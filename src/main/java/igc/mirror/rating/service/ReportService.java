package igc.mirror.rating.service;

import igc.mirror.auth.UserDetails;
import igc.mirror.config.LoggingConstants;
import igc.mirror.exception.common.RemoteServiceCallException;
import igc.mirror.version.service.RemoteRequestVersionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyExtractors;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.function.Predicate;

@Service
public class ReportService {
    static final Logger logger = LoggerFactory.getLogger(RemoteRequestVersionService.class);
    private final String RATING_REPORT_URL = "/rating";

    @Value("${mirror.application.user-agent}")
    private String userAgent;
    private final UserDetails userDetails;

    private final WebClient webClientReport;

    public ReportService(UserDetails userDetails,
                         @Qualifier("report") WebClient webClientReport) {
        this.userDetails = userDetails;
        this.webClientReport = webClientReport;
    }

    /**
     * Выгрузка отчета "Обратная связь" в excel
     * *
     */
    public ResponseEntity<Resource> getExcelRatingReport(LocalDate from, LocalDate to) {
        logger.info("Выгрузка отчета \"Обратная связь\". Вызов удаленного сервиса report");
        //задаем параметры
        UriComponentsBuilder urlBuilder = UriComponentsBuilder.fromUriString(RATING_REPORT_URL);
        if (from != null) urlBuilder.queryParam("from", from);
        if (to != null) urlBuilder.queryParam("to", to);
        String uri = urlBuilder.encode().toUriString();

        return webClientReport
                .post()
                .uri(uri)
                .header(HttpHeaders.USER_AGENT, userAgent)
                .header(LoggingConstants.X_REQUEST_ID_HEADER, MDC.get(LoggingConstants.X_REQUEST_ID_KEY))
                .header(HttpHeaders.AUTHORIZATION, String.format("Bearer %s", userDetails.getJwtTokenValue()))
                .retrieve()
                .onStatus(
                        HttpStatusCode::is4xxClientError,
                        response -> Mono.error(new RemoteServiceCallException("Сервис " + uri + " не найден", response.statusCode(), uri, response.body(BodyExtractors.toDataBuffers()).toString())))
                .onStatus(
                        HttpStatusCode::is5xxServerError,
                        response -> Mono.error(new RemoteServiceCallException("Сервис " + uri + " не доступен", response.statusCode(), uri, response.body(BodyExtractors.toDataBuffers()).toString())))
                .toEntity(new ParameterizedTypeReference<Resource>() {
                })
                .onErrorMap(WebClientRequestException.class, throwable -> new RemoteServiceCallException("Неизвестный url", HttpStatus.INTERNAL_SERVER_ERROR, uri, throwable.getMessage()))
                .onErrorMap(Predicate.not(RemoteServiceCallException.class::isInstance),
                        throwable -> new RemoteServiceCallException("Ошибка обработки данных при получении ответа", HttpStatus.BAD_REQUEST, uri, throwable.getMessage()))
                .doOnError(err -> logger.error("Ошибка получения данных удаленного сервиса - {}", err.getMessage()))
                .log()
                .block();
    }

}
