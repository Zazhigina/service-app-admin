package igc.mirror.task;

import igc.mirror.auth.client.KeycloakAuthClient;
import igc.mirror.auth.dto.AuthResponseDto;
import igc.mirror.config.LoggingConstants;
import igc.mirror.exception.common.RemoteServiceCallException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientRequestException;

import java.util.UUID;

import static igc.mirror.config.LoggingConstants.USER_AGENT_KEY;
import static igc.mirror.config.LoggingConstants.X_REQUEST_ID_KEY;

public class UpdatePricingTask {
    static final Logger logger = LoggerFactory.getLogger(UpdatePricingTask.class);

    @Autowired
    @Qualifier("integration")
    private WebClient webClient;

    @Autowired
    private KeycloakAuthClient keycloakAuthClient;

    @Value("${mirror.schedule.tasks.username}")
    private String scheduleTasksUserName;

    @Value("${mirror.schedule.tasks.password}")
    private String scheduleTasksPassword;

    @Value("${mirror.application.user-agent}")
    private String userAgent;

    @Scheduled(cron = "${mirror.schedule.tasks.cron.daily-integration-uploading}")
    public void sendUpdatePricingRequest() {
        MDC.put(USER_AGENT_KEY, userAgent);
        MDC.put(X_REQUEST_ID_KEY, UUID.randomUUID().toString());

        logger.info("Запуск обновления списка прайсингов из Профессионалы 4.0");

        AuthResponseDto authResponseDto = keycloakAuthClient.authenticate(scheduleTasksUserName, scheduleTasksPassword);

        String uri = "effect/exchange/professional/roles";

        webClient
                .post()
                .uri(uri)
                .header("Authorization", "Bearer " + authResponseDto.getAccessToken())
                .header(HttpHeaders.USER_AGENT, userAgent)
                .header(LoggingConstants.X_REQUEST_ID_HEADER, MDC.get(X_REQUEST_ID_KEY))
                .retrieve()
                .bodyToMono(Void.class)
                .log()
                .onErrorMap(WebClientRequestException.class, throwable -> new RemoteServiceCallException("Неизвестный url", HttpStatus.INTERNAL_SERVER_ERROR, uri, throwable.getMessage()))
                .doOnError(err -> logger.error("Ошибка запуска удаленного сервиса - {}", err.getMessage()))
                .doOnSuccess(success -> logger.info("Обновление списка прайсингов из Профессионалы 4.0 завершено"))
                .log()
                .block();

        MDC.remove(USER_AGENT_KEY);
        MDC.remove(X_REQUEST_ID_KEY);
    }
}
