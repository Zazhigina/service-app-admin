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

public class CommercialOfferStatusTask {
    static final Logger logger = LoggerFactory.getLogger(CommercialOfferStatusTask.class);

    private static final String EP_REQUEST_SERVICE = "nmc/request";

    @Autowired
    @Qualifier("ep")
    private WebClient webClient;

    @Autowired
    private KeycloakAuthClient keycloakAuthClient;

    @Value("${mirror.schedule.tasks.username}")
    private String scheduleTasksUserName;

    @Value("${mirror.schedule.tasks.password}")
    private String scheduleTasksPassword;

    @Value("${mirror.application.user-agent}")
    private String userAgent;

    @Scheduled(cron = "${mirror.schedule.tasks.cron.change-status-offer-end}")
    public void changeStatusAfterCommercialOfferEnd() {
        MDC.put(USER_AGENT_KEY, userAgent);
        MDC.put(X_REQUEST_ID_KEY, UUID.randomUUID().toString());

        logger.info("Запуск задания по изменению статусов КП и запроса НМЦ после окончания срока приема коммерческих предложений.");

        AuthResponseDto authResponseDto = keycloakAuthClient.authenticate(scheduleTasksUserName, scheduleTasksPassword);

        changeCommercialOfferStatusToExpired(authResponseDto);
        changeCommercialOfferStatusToReceived(authResponseDto);
        changeRequestClassToCommercialOfferReceived(authResponseDto);

        MDC.remove(USER_AGENT_KEY);
        MDC.remove(X_REQUEST_ID_KEY);
    }

    private void changeCommercialOfferStatusToExpired(AuthResponseDto authResponseDto) {
        String uri = String.join("/", EP_REQUEST_SERVICE, "contractor/commercial-offer-status/expired");

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
                .doOnSuccess(success -> logger.info("Обновление статуса КП контрагента после истечения срока ответа на \"Срок предоставления истек\" выполнено."))
                .log()
                .block();
    }

    private void changeCommercialOfferStatusToReceived(AuthResponseDto authResponseDto) {
        String uri = String.join("/", EP_REQUEST_SERVICE, "contractor/commercial-offer-status/received");

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
                .doOnSuccess(success -> logger.info("Обновление статуса КП контрагента после истечения срока ответа на \"КП получено\" выполнено."))
                .log()
                .block();
    }

    private void changeRequestClassToCommercialOfferReceived(AuthResponseDto authResponseDto) {
        String uri = String.join("/", EP_REQUEST_SERVICE, "request-class/commercial-offer-received");

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
                .doOnSuccess(success -> logger.info("Обновление статуса запроса НМЦ после окончания срока приема КП выполнено."))
                .log()
                .block();
    }
}
