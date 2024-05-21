package igc.mirror.param.service;

import igc.mirror.auth.UserDetails;
import igc.mirror.auth.dto.AuthResponseDto;
import igc.mirror.config.LoggingConstants;
import igc.mirror.exception.common.RemoteServiceCallException;
import igc.mirror.task.RequestSearchHistoryTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyExtractors;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.UUID;
import java.util.function.Predicate;

import static igc.mirror.config.LoggingConstants.USER_AGENT_KEY;
import static igc.mirror.config.LoggingConstants.X_REQUEST_ID_KEY;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Service
public class RemoteParamService {
    static final Logger logger = LoggerFactory.getLogger(RemoteParamService.class);

    @Value("${mirror.application.user-agent}")
    private String userAgent;

    @Autowired
    @Qualifier("ma")
    private WebClient webClientMa;

    @Autowired
    @Qualifier("ep")
    private WebClient webClientEp;

    @Autowired
    @Qualifier("chat")
    private WebClient webClientChat;

    @Autowired
    private UserDetails userDetails;

    public void clearMaParameterCache() {

        logger.info("Сброс кеша параметров приложения ma");

        String uri = String.join("/", "", "param/cache");

        webClientMa
                .delete()
                .uri(uri)
                .header(HttpHeaders.CONTENT_TYPE, APPLICATION_JSON_VALUE)
                .header(HttpHeaders.USER_AGENT, userAgent)
                .header(LoggingConstants.X_REQUEST_ID_HEADER, MDC.get(LoggingConstants.X_REQUEST_ID_KEY))
                .header(HttpHeaders.AUTHORIZATION, String.format("Bearer %s", userDetails.getJwtTokenValue()))
                .accept(MediaType.APPLICATION_JSON)
                .acceptCharset(StandardCharsets.UTF_8)
                .retrieve()
                .bodyToMono(Void.class)
                .log()
                .onErrorMap(WebClientRequestException.class, throwable -> new RemoteServiceCallException("Неизвестный url", HttpStatus.INTERNAL_SERVER_ERROR, uri, throwable.getMessage()))
                .doOnError(err -> logger.error("Ошибка запуска удаленного сервиса {} - {}", uri, err.getMessage()))
                .doOnSuccess(success -> logger.info("Обновление кеша ma завершено."))
                .subscribe();
    }

    public void clearEpParameterCache() {

        logger.info("Сброс кеша параметров приложения ep");

        String uri = String.join("/", "", "param/cache");

        webClientEp
                .delete()
                .uri(uri)
                .header(HttpHeaders.CONTENT_TYPE, APPLICATION_JSON_VALUE)
                .header(HttpHeaders.USER_AGENT, userAgent)
                .header(LoggingConstants.X_REQUEST_ID_HEADER, MDC.get(LoggingConstants.X_REQUEST_ID_KEY))
                .header(HttpHeaders.AUTHORIZATION, String.format("Bearer %s", userDetails.getJwtTokenValue()))
                .accept(MediaType.APPLICATION_JSON)
                .acceptCharset(StandardCharsets.UTF_8)
                .retrieve()
                .bodyToMono(Void.class)
                .log()
                .onErrorMap(WebClientRequestException.class, throwable -> new RemoteServiceCallException("Неизвестный url", HttpStatus.INTERNAL_SERVER_ERROR, uri, throwable.getMessage()))
                .doOnError(err -> logger.error("Ошибка запуска удаленного сервиса {} - {}", uri, err.getMessage()))
                .doOnSuccess(success -> logger.info("Обновление кеша ma завершено."))
                .subscribe();
    }

    public void clearChatParameterCache() {

        logger.info("Сброс кеша параметров приложения chat");

        String uri = String.join("/", "", "param/cache");

        webClientChat
                .delete()
                .uri(uri)
                .header(HttpHeaders.CONTENT_TYPE, APPLICATION_JSON_VALUE)
                .header(HttpHeaders.USER_AGENT, userAgent)
                .header(LoggingConstants.X_REQUEST_ID_HEADER, MDC.get(LoggingConstants.X_REQUEST_ID_KEY))
                .header(HttpHeaders.AUTHORIZATION, String.format("Bearer %s", userDetails.getJwtTokenValue()))
                .accept(MediaType.APPLICATION_JSON)
                .acceptCharset(StandardCharsets.UTF_8)
                .retrieve()
                .bodyToMono(Void.class)
                .log()
                .onErrorMap(WebClientRequestException.class, throwable -> new RemoteServiceCallException("Неизвестный url", HttpStatus.INTERNAL_SERVER_ERROR, uri, throwable.getMessage()))
                .doOnError(err -> logger.error("Ошибка запуска удаленного сервиса {} - {}", uri, err.getMessage()))
                .doOnSuccess(success -> logger.info("Обновление кеша chat завершено."))
                .subscribe();
    }
}
