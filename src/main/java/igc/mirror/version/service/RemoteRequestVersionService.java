package igc.mirror.version.service;

import igc.mirror.auth.UserDetails;
import igc.mirror.config.LoggingConstants;
import igc.mirror.exception.common.RemoteServiceCallException;
import igc.mirror.exception.handler.SuccessInfo;
import igc.mirror.service.dto.RestPage;
import igc.mirror.utils.qfilter.DataFilter;
import igc.mirror.utils.web.WebServiceUtil;
import igc.mirror.version.dto.RequestVersionProcessingStatus;
import igc.mirror.version.dto.RequestVersionRating;
import igc.mirror.version.filter.RequestVersionRatingSearchCriteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyExtractors;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import reactor.core.publisher.Mono;

import java.util.function.Predicate;

@Service
public class RemoteRequestVersionService {
    static final Logger logger = LoggerFactory.getLogger(RemoteRequestVersionService.class);
    static final String REQUEST_VERSION_SERVICE = "/request";
    @Value("${mirror.application.user-agent}")
    private String userAgent;
    private final UserDetails userDetails;
    private final WebClient webClientMa;
    private final WebClient webClientReport;
    private final WebServiceUtil webServiceUtil;

    public RemoteRequestVersionService(UserDetails userDetails,
                                       @Qualifier("ma") WebClient webClientMa,
                                       @Qualifier("report") WebClient webClientReport,
                                       WebServiceUtil webServiceUtil) {
        this.userDetails = userDetails;
        this.webClientMa = webClientMa;
        this.webClientReport = webClientReport;
        this.webServiceUtil = webServiceUtil;
    }

    /**
     * Получить данные оценок версий поиска по переданным критериям
     *
     * @param filter   критерии отбора данных
     * @param pageable настройки пагинации
     * @return данные оценок версий поиска
     */
    public Page<RequestVersionRating> findRequestVersionRatingByFilters(DataFilter<RequestVersionRatingSearchCriteria> filter,
                                                                        Pageable pageable) {

        logger.info("Получение данных оценок версий поиска по переданным критериям. Вызов сервиса MA с параметрами {}", filter);

        String uri = String.join("/", REQUEST_VERSION_SERVICE, "/version-rating/filter");
        String urlTemplate = webServiceUtil.buildUriByPageableProperties(uri, pageable);

        return webClientMa
                .post()
                .uri(urlTemplate)
                .header(HttpHeaders.USER_AGENT, userAgent)
                .header(LoggingConstants.X_REQUEST_ID_HEADER, MDC.get(LoggingConstants.X_REQUEST_ID_KEY))
                .header(HttpHeaders.AUTHORIZATION, String.format("Bearer %s", userDetails.getJwtTokenValue()))
                .body(Mono.just(filter), DataFilter.class)
                .retrieve()
                .onStatus(
                        HttpStatusCode::is4xxClientError,
                        response -> Mono.error(new RemoteServiceCallException("Сервис " + uri + " не найден", response.statusCode(), uri, response.body(BodyExtractors.toDataBuffers()).toString())))
                .onStatus(
                        HttpStatusCode::is5xxServerError,
                        response -> Mono.error(new RemoteServiceCallException("Сервис " + uri + " не доступен", response.statusCode(), uri, response.body(BodyExtractors.toDataBuffers()).toString())))
                .bodyToMono(new ParameterizedTypeReference<RestPage<RequestVersionRating>>() {
                })
                .onErrorMap(WebClientRequestException.class, throwable -> new RemoteServiceCallException("Неизвестный url", HttpStatus.INTERNAL_SERVER_ERROR, uri, throwable.getMessage()))
                .onErrorMap(Predicate.not(RemoteServiceCallException.class::isInstance),
                        throwable -> new RemoteServiceCallException("Ошибка обработки данных при получении ответа", HttpStatus.BAD_REQUEST, uri, throwable.getMessage()))
                .doOnError(err -> logger.error("Ошибка получения данных удаленного сервиса - {}", err.getMessage()))
                .log()
                .block();
    }

    /**
     * Изменить статус обработки версии поиска
     *
     * @param versionProcessingStatus данные для изменения статуса версии поиска
     * @return информация об успешном изменении
     */
    public SuccessInfo changeRequestVersionProcessingStatus(RequestVersionProcessingStatus versionProcessingStatus) {

        logger.info("Изменение статуса обработки версии поиска. Вызов сервиса MA с параметрами {}", versionProcessingStatus);

        String uri = String.join("/", REQUEST_VERSION_SERVICE, "version/processing-status");

        return webClientMa
                .put()
                .uri(uri)
                .header(HttpHeaders.USER_AGENT, userAgent)
                .header(LoggingConstants.X_REQUEST_ID_HEADER, MDC.get(LoggingConstants.X_REQUEST_ID_KEY))
                .header(HttpHeaders.AUTHORIZATION, String.format("Bearer %s", userDetails.getJwtTokenValue()))
                .body(Mono.just(versionProcessingStatus), RequestVersionProcessingStatus.class)
                .retrieve()
                .onStatus(
                        HttpStatusCode::is4xxClientError,
                        response -> Mono.error(new RemoteServiceCallException("Сервис " + uri + " не найден", response.statusCode(), uri, response.body(BodyExtractors.toDataBuffers()).toString())))
                .onStatus(
                        HttpStatusCode::is5xxServerError,
                        response -> Mono.error(new RemoteServiceCallException("Сервис " + uri + " не доступен", response.statusCode(), uri, response.body(BodyExtractors.toDataBuffers()).toString())))
                .bodyToMono(SuccessInfo.class)
                .onErrorMap(WebClientRequestException.class, throwable -> new RemoteServiceCallException("Неизвестный url", HttpStatus.INTERNAL_SERVER_ERROR, uri, throwable.getMessage()))
                .onErrorMap(Predicate.not(RemoteServiceCallException.class::isInstance),
                        throwable -> new RemoteServiceCallException("Ошибка обработки данных при получении ответа", HttpStatus.BAD_REQUEST, uri, throwable.getMessage()))
                .doOnError(err -> logger.error("Ошибка получения данных удаленного сервиса - {}", err.getMessage()))
                .log()
                .block();
    }

    /**
     * Выгрузка отчета "Перечень найденных поставщиков" для версии поиска в excel
     *
     * @param requestVersionId, идентификатор версии поиска
     * @return файл excel "Перечень найденных поставщиков"
     */
    public ResponseEntity<Resource> getExcelReportRequestContractorVersion(Long requestVersionId) {
        logger.info("Выгрузка отчета \"Перечень найденных поставщиков\" для версии поиска {} в excel. Вызов сервиса REPORT", requestVersionId);

        String uri = String.join("/", REQUEST_VERSION_SERVICE, "version", String.valueOf(requestVersionId), "contractor");

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
