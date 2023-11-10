package igc.mirror.task;

import igc.mirror.auth.client.KeycloakAuthClient;
import igc.mirror.auth.dto.AuthResponseDto;
import igc.mirror.config.LoggingConstants;
import igc.mirror.exception.common.RemoteServiceCallException;
import igc.mirror.param.service.ParamService;
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

public class CommercialOfferNotificationTask {
    static final Logger logger = LoggerFactory.getLogger(CommercialOfferNotificationTask.class);

    private static final String EP_COMMERCIAL_OFFER_SERVICE = "commercial-offer-message";
    private static final String SHEDULLER_MAILING_ALLOWED_PARAM = "SHEDULLER.MAILING_ALLOWED"; //todo create ApplicationParameter enum?
    private static final String MAILING_NOT_ALLOWED = "0";

    @Autowired
    @Qualifier("ep")
    private WebClient webClient;

    @Autowired
    private KeycloakAuthClient keycloakAuthClient;

    @Autowired
    ParamService paramService;

    @Value("${mirror.schedule.tasks.username}")
    private String scheduleTasksUserName;

    @Value("${mirror.schedule.tasks.password}")
    private String scheduleTasksPassword;

    @Value("${mirror.application.user-agent}")
    private String userAgent;

    @Scheduled(cron = "${mirror.schedule.tasks.cron.notify-offer-end}")
    public void notifyCommercialOfferEnd() {
        MDC.put(USER_AGENT_KEY, userAgent);
        MDC.put(X_REQUEST_ID_KEY, UUID.randomUUID().toString());

        logger.info("Запуск задания по отправке уведомлений об окончании приема коммерческих предложений.");

        if (paramService.findByKey(SHEDULLER_MAILING_ALLOWED_PARAM).getVal().equals(MAILING_NOT_ALLOWED))
            logger.info("Возможность рассылки отключена. Для отправки уведомлений измените настройки.");
        else {
            AuthResponseDto authResponseDto = keycloakAuthClient.authenticate(scheduleTasksUserName, scheduleTasksPassword);

            sendOfferComingEndContractorMessage(authResponseDto);
            sendOfferComingEndInitiatorMessage(authResponseDto);
            sendOfferCompletedContractorMessage(authResponseDto);
            sendOfferCompletedInitiatorMessage(authResponseDto);
        }

        MDC.remove(USER_AGENT_KEY);
        MDC.remove(X_REQUEST_ID_KEY);
    }

    private void sendOfferComingEndContractorMessage(AuthResponseDto authResponseDto) {
        String uri = String.join("/", EP_COMMERCIAL_OFFER_SERVICE, "coming-end/contractor");

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
                .doOnSuccess(success -> logger.info("Отправка уведомления контрагентам о завершающемся приеме КП выполнена."))
                .log()
                .block();
    }

    private void sendOfferComingEndInitiatorMessage(AuthResponseDto authResponseDto) {
        String uri = String.join("/", EP_COMMERCIAL_OFFER_SERVICE, "coming-end/initiator");

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
                .doOnSuccess(success -> logger.info("Отправка уведомления инициаторам о завершающемся приеме КП выполнена."))
                .log()
                .block();
    }

    private void sendOfferCompletedContractorMessage(AuthResponseDto authResponseDto) {
        String uri = String.join("/", EP_COMMERCIAL_OFFER_SERVICE, "completed/contractor");

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
                .doOnSuccess(success -> logger.info("Отправка уведомления контрагентам о завершившемся приеме КП выполнена."))
                .log()
                .block();
    }

    private void sendOfferCompletedInitiatorMessage(AuthResponseDto authResponseDto) {
        String uri = String.join("/", EP_COMMERCIAL_OFFER_SERVICE, "completed/initiator");

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
                .doOnSuccess(success -> logger.info("Отправка уведомления инициаторам о завершившемся приеме КП выполнена."))
                .log()
                .block();
    }
}