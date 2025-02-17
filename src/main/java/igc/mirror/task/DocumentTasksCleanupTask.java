package igc.mirror.task;

import igc.mirror.auth.client.KeycloakAuthClient;
import igc.mirror.auth.dto.AuthResponseDto;
import igc.mirror.config.LoggingConstants;
import igc.mirror.exception.common.RemoteServiceCallException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

@Slf4j
@RequiredArgsConstructor
@Component
public class DocumentTasksCleanupTask {
    private final KeycloakAuthClient keycloakAuthClient;

    @Value("${mirror.schedule.tasks.username}")
    private String scheduleTasksUserName;
    @Value("${mirror.schedule.tasks.password}")
    private String scheduleTasksPassword;
    @Value("${mirror.application.user-agent}")
    private String userAgent;

    @Autowired
    @Qualifier("doc")
    private WebClient webClient;

    @Scheduled(cron = "${mirror.schedule.tasks.cron.document-task-cleanup}")
    public void clearDocumentTasks(){
        MDC.put(USER_AGENT_KEY, userAgent);
        MDC.put(X_REQUEST_ID_KEY, UUID.randomUUID().toString());

        log.info("Запуск очистки задач на формирование документов в сервисе документов");

        AuthResponseDto authResponseDto = keycloakAuthClient.authenticate(scheduleTasksUserName, scheduleTasksPassword);

        String uri = "tasks/cleanup";

        webClient
                .post()
                .uri(uri)
                .headers(httpHeaders -> {
                    httpHeaders.setBearerAuth(authResponseDto.getAccessToken());
                    httpHeaders.add(HttpHeaders.USER_AGENT, userAgent);
                    httpHeaders.add(LoggingConstants.X_REQUEST_ID_HEADER, MDC.get(X_REQUEST_ID_KEY));
                })
                .retrieve()
                .bodyToMono(Void.class)
                .log()
                .onErrorMap(WebClientRequestException.class, throwable -> new RemoteServiceCallException("Неизвестный url", HttpStatus.INTERNAL_SERVER_ERROR, uri, throwable.getMessage()))
                .doOnError(err -> log.error("Ошибка запуска удаленного сервиса", err))
                .doOnSuccess(success -> log.info("Очистка выполнена успешно"))
                .log()
                .block();

        MDC.remove(USER_AGENT_KEY);
        MDC.remove(X_REQUEST_ID_KEY);
    }
}
