package igc.mirror.prcat.service;

import com.fasterxml.jackson.databind.JsonNode;
import igc.mirror.auth.UserDetails;
import igc.mirror.config.LoggingConstants;
import igc.mirror.exception.common.RemoteServiceCallException;
import igc.mirror.prcat.dto.PrcatServiceVersionDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyExtractors;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.function.Predicate;

@Service
public class PrcatService {

    @Value("${mirror.application.user-agent}")
    private String userAgent;

    private static final String BEARER_FORMAT = "Bearer %s";
    private static final String UNKNOWN_URL_ERROR = "Неизвестный url";
    private static final String DATA_PROCESSING_ERROR = "Ошибка обработки данных при получении ответа";
    private static final String DATA_RECEIVING_ERROR = "Ошибка получения данных удаленного сервиса";

    static final Logger logger = LoggerFactory.getLogger(PrcatService.class);

    private final UserDetails userDetails;
    private final WebClient webClient;

    @Autowired
    public PrcatService(UserDetails userDetails, @Qualifier("prcat") WebClient webClient) {
        this.userDetails = userDetails;
        this.webClient = webClient;
    }

    public JsonNode sendServiceVersions(List<PrcatServiceVersionDto> versions) {
        logger.info("Отправка данных маппинга услуг справочника КТ-777. Вызов сервиса Справочника расценок.");
        String uri = String.join("/", "nsi", "service-version");

        return webClient
                .post()
                .uri(uri)
                .header(HttpHeaders.AUTHORIZATION, String.format(BEARER_FORMAT, userDetails.getJwtTokenValue()))
                .header(HttpHeaders.USER_AGENT, userAgent)
                .header(LoggingConstants.X_REQUEST_ID_HEADER, MDC.get(LoggingConstants.X_REQUEST_ID_KEY))
                .body(Mono.just(versions), PrcatServiceVersionDto.class)
                .retrieve()
                .onStatus(HttpStatusCode::isError,
                        response -> Mono.error(
                                new RemoteServiceCallException(
                                        buildHttpCodeErrorMessage(uri, response.statusCode()),
                                        response.statusCode(),
                                        uri,
                                        response.body(BodyExtractors.toDataBuffers()).toString())
                        )
                )
                .bodyToMono(JsonNode.class)
                .onErrorMap(WebClientRequestException.class,
                        throwable -> new RemoteServiceCallException(
                                UNKNOWN_URL_ERROR,
                                HttpStatus.INTERNAL_SERVER_ERROR,
                                uri,
                                throwable.getMessage()
                        )
                )
                .onErrorMap(Predicate.not(RemoteServiceCallException.class::isInstance),
                        throwable -> new RemoteServiceCallException(
                                DATA_PROCESSING_ERROR,
                                HttpStatus.BAD_REQUEST,
                                uri, throwable.getMessage()
                        )
                )
                .doOnError(err -> logger.error(DATA_RECEIVING_ERROR, err))
                .log()
                .block();
    }



    private String buildHttpCodeErrorMessage(String uri, HttpStatusCode statusCode) {
        StringBuilder sb = new StringBuilder("Сервис ");
        sb.append(uri);
        if (statusCode.is4xxClientError())
            sb.append(" не найден");
        if (statusCode.is5xxServerError())
            sb.append(" не доступен");
        return sb.toString();
    }

}
