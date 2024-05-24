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

public class NotificationServiceTask {
    static final Logger logger = LoggerFactory.getLogger(NotificationServiceTask.class);

    @Autowired
    @Qualifier("notification")
    private WebClient webClient;

    @Autowired
    private KeycloakAuthClient keycloakAuthClient;

    @Value("${mirror.schedule.tasks.username}")
    private String scheduleTasksUserName;

    @Value("${mirror.schedule.tasks.password}")
    private String scheduleTasksPassword;

    @Value("${mirror.application.user-agent}")
    private String userAgent;

    @Scheduled(cron = "${mirror.schedule.tasks.cron.notification-process}")
    public void processingNotifications() {

        MDC.put(USER_AGENT_KEY, userAgent);
        MDC.put(X_REQUEST_ID_KEY, UUID.randomUUID().toString());

        logger.info("Запуск задания по обработке уведомлений.");

        AuthResponseDto authResponseDto = keycloakAuthClient.authenticate(scheduleTasksUserName, scheduleTasksPassword);

        //Помечаем на удаление уведомления с истекшим сроком хранения
        try {
            deleteNoticesDurationEnd(authResponseDto);
        } catch (Exception e) {
            //ignore
        }

        MDC.remove(USER_AGENT_KEY);
        MDC.remove(X_REQUEST_ID_KEY);
    }

    private void deleteNoticesDurationEnd(AuthResponseDto authResponseDto) {

        String uri = new String("/notice/duration-end/deleting");

        webClient
                .put()
                .uri(uri)
                .header("Authorization", "Bearer " + authResponseDto.getAccessToken())
                .header(HttpHeaders.USER_AGENT, userAgent)
                .header(LoggingConstants.X_REQUEST_ID_HEADER, MDC.get(X_REQUEST_ID_KEY))
                .retrieve()
                .bodyToMono(Void.class)
                .log()
                .onErrorMap(WebClientRequestException.class, throwable -> new RemoteServiceCallException("Неизвестный url", HttpStatus.INTERNAL_SERVER_ERROR, uri, throwable.getMessage()))
                .doOnError(err -> logger.error("Ошибка запуска удаленного сервиса - {}", err.getMessage()))
                .doOnSuccess(success -> logger.info("Уведомления с истекшим сроком хранения отмечены как удаленные."))
                .log()
                .block();
    }
}
