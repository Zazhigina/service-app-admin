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

public class RequestSearchHistoryTask {
    static final Logger logger = LoggerFactory.getLogger(RequestSearchHistoryTask.class);

    @Autowired
    @Qualifier("ma")
    private WebClient webClient;

    @Autowired
    private KeycloakAuthClient keycloakAuthClient;

    @Value("${mirror.schedule.tasks.username}")
    private String scheduleTasksUserName;

    @Value("${mirror.schedule.tasks.password}")
    private String cheduleTasksPassword;

    @Value("${mirror.application.user-agent}")
    private String userAgent;

    //@Scheduled(cron = "0 0/30 23 * * *")
    @Scheduled(cron = "0 20 15 * * *")
    public void sendRequestSearchHistory() {
        logger.info("Подготовка к запуску задания по сбору и отправке истории поиска в Python, user-agent {}", userAgent);

        AuthResponseDto authResponseDto = keycloakAuthClient.authenticate(scheduleTasksUserName, cheduleTasksPassword);

        String uri = String.join("/", "", "request/history/send");

        webClient
                .post()
                .uri(uri)
                .header("Authorization", "Bearer " + authResponseDto.getAccessToken())
                .header(HttpHeaders.USER_AGENT, userAgent)
                .header(LoggingConstants.X_REQUEST_ID_HEADER, MDC.get(LoggingConstants.X_REQUEST_ID_KEY))
                .retrieve()
                .bodyToMono(Void.class)
                .log()
                .onErrorMap(WebClientRequestException.class, throwable -> new RemoteServiceCallException("Неизвестный url", HttpStatus.INTERNAL_SERVER_ERROR, uri, throwable.getMessage()))
                .doOnError(err -> logger.error("Ошибка запуска удаленного сервиса - {}", err.getMessage()))
                .doOnSuccess(success -> logger.info("Задание по сбору и отправке истории поиска в Python запущено."))
                .subscribe();
    }
}
