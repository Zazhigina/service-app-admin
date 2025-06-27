package igc.mirror.task;

import igc.mirror.auth.client.KeycloakAuthClient;
import igc.mirror.auth.dto.AuthResponseDto;
import igc.mirror.config.LoggingConstants;
import igc.mirror.exception.common.RemoteServiceCallException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientRequestException;

import java.util.UUID;

import static igc.mirror.config.LoggingConstants.USER_AGENT_KEY;
import static igc.mirror.config.LoggingConstants.X_REQUEST_ID_KEY;

//Новая задача
@Slf4j
@Component
@RequiredArgsConstructor
public class MonitoringStatisticsCleanupTask {
    static final Logger logger = LoggerFactory.getLogger(MonitoringStatisticsCleanupTask.class);

    @Autowired
    private KeycloakAuthClient keycloakAuthClient;

    @Autowired
    @Qualifier("doc")
    private WebClient webClient;

    @Value("${mirror.schedule.tasks.username}")
    private String scheduleTasksUserName;

    @Value("${mirror.schedule.tasks.password}")
    private String scheduleTasksPassword;

    @Value("${mirror.application.user-agent}")
    private String userAgent;

    @Scheduled(cron = "${mirror.schedule.tasks.cron.notify-offer-end}")
    public void cleanupOldStatistics() {
        MDC.put(USER_AGENT_KEY, userAgent);
        MDC.put(X_REQUEST_ID_KEY, UUID.randomUUID().toString());

        logger.info("Запуск очистки статистики мониторинга старше 2 недель");

        AuthResponseDto authResponse = keycloakAuthClient.authenticate(scheduleTasksUserName, scheduleTasksPassword);

        String uri = "/monitoring/statistics/cleanup";

        webClient
                .delete()
                .uri(uri)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + authResponse.getAccessToken())
                .header(HttpHeaders.USER_AGENT, userAgent)
                .header(LoggingConstants.X_REQUEST_ID_HEADER, MDC.get(X_REQUEST_ID_KEY))
                .retrieve()
                .bodyToMono(Void.class)
                .onErrorMap(WebClientRequestException.class, throwable -> new RemoteServiceCallException("Неизвестный url", HttpStatus.INTERNAL_SERVER_ERROR, uri, throwable.getMessage()))
                .doOnSuccess(res -> logger.info("Статистика успешно очищена"))
                .doOnError(err -> logger.error("Ошибка при очистке: {}", err.getMessage()))
                .log()
                .block();

        MDC.remove(USER_AGENT_KEY);
        MDC.remove(X_REQUEST_ID_KEY);
    }
}