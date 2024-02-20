package igc.mirror.nsi.service;

import igc.mirror.auth.UserDetails;
import igc.mirror.config.LoggingConstants;
import igc.mirror.exception.common.RemoteServiceCallException;
import igc.mirror.nsi.model.ServiceProduct;
import igc.mirror.segment.ref.SegmentRecordType;
import igc.mirror.segment.view.SegmentDto;
import igc.mirror.segment.view.ServiceSegmentSubsegmentDto;
import igc.mirror.service.dto.RestPage;
import igc.mirror.service.dto.ServiceVersionDTO;
import igc.mirror.service.dto.ServiceVersionReadDto;
import igc.mirror.service.exchange.ReferenceSavingResult;
import igc.mirror.service.filter.SegmentSearchCriteria;
import igc.mirror.service.filter.ServiceProductSearchCriteria;
import igc.mirror.service.filter.ServiceVersionSearchCriteria;
import igc.mirror.utils.qfilter.DataFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyExtractors;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

@Service
public class NSIService {
    static final Logger logger = LoggerFactory.getLogger(NSIService.class);
    static final String REFERENCE_SERVICE = "/reference";
    @Value("${mirror.application.user-agent}")
    private String userAgent;

    @Autowired
    UserDetails userDetails;

    @Autowired
    @Qualifier("nsi")
    private WebClient webClient;

    /**
     * Получение записей работ-услуг по кодам
     * {@linkplain //mirror.inlinegroup-c.ru/api/nsi}
     *
     * @param codes коды работ-услуг
     * @return Данные работ-услуг
     */
    public Map<String, ServiceProduct> getServicesProductsByCodes(List<String> codes) {
        logger.info("Получение данных справочника услуг. Вызов сервиса НСИ параметрами {}", codes);

        String uri = String.join("/", REFERENCE_SERVICE, "service-product/map");

        return webClient
                .post()
                .uri(uri)
                .header(HttpHeaders.USER_AGENT, userAgent)
                .header(LoggingConstants.X_REQUEST_ID_HEADER, MDC.get(LoggingConstants.X_REQUEST_ID_KEY))
                .header(HttpHeaders.AUTHORIZATION, String.format("Bearer %s", userDetails.getJwtTokenValue()))
                .body(Mono.just(codes), List.class)
                .retrieve()
                .onStatus(
                        HttpStatusCode::is4xxClientError,
                        response -> Mono.error(new RemoteServiceCallException("Сервис " + uri + " не найден", response.statusCode(), uri, response.body(BodyExtractors.toDataBuffers()).toString())))
                .onStatus(
                        HttpStatusCode::is5xxServerError,
                        response -> Mono.error(new RemoteServiceCallException("Сервис " + uri + " не доступен", response.statusCode(), uri, response.body(BodyExtractors.toDataBuffers()).toString())))
                .bodyToMono(new ParameterizedTypeReference<Map<String, ServiceProduct>>() {
                })
                .onErrorMap(WebClientRequestException.class, throwable -> new RemoteServiceCallException("Неизвестный url", HttpStatus.INTERNAL_SERVER_ERROR, uri, throwable.getMessage()))
                .onErrorMap(Predicate.not(RemoteServiceCallException.class::isInstance),
                        throwable -> new RemoteServiceCallException("Ошибка обработки данных при получении ответа", HttpStatus.BAD_REQUEST, uri, throwable.getMessage()))
                .doOnError(err -> logger.error("Ошибка получения данных удаленного сервиса - {}", err.getMessage()))
                .log()
                .block();
    }

    /**
     * Получение записей работ-услуг по фильтру
     * {@linkplain //mirror.inlinegroup-c.ru/api/nsi}
     *
     * @param filter фильтр
     * @param params педжинация
     * @return Данные работ-услуг
     */
    public Page<ServiceProduct> getServicesProductsByFilter(DataFilter<ServiceProductSearchCriteria> filter, MultiValueMap<String, String> params) {
        logger.info("Получение данных справочника услуг. Вызов сервиса НСИ с фильтром");

        String uri = String.join("/", REFERENCE_SERVICE, "service-product/filter");
        String urlTemplate = UriComponentsBuilder.fromUriString(uri)
                .queryParams(params)
                .encode()
                .toUriString();


        return webClient
                .post()
//                .uri(uriBuilder -> uriBuilder
//                        .path(uri)
//                        .queryParams(params)
//                        .build())
                .uri(urlTemplate)
                .header(HttpHeaders.USER_AGENT, userAgent)
                .header(LoggingConstants.X_REQUEST_ID_HEADER, MDC.get(LoggingConstants.X_REQUEST_ID_KEY))
                .header(HttpHeaders.AUTHORIZATION, String.format("Bearer %s", userDetails.getJwtTokenValue()))
                .body(Mono.just(filter), DataFilter.class)
                .retrieve()

//                .toEntity(new ParameterizedTypeReference<PageImpl<ServiceProduct>>() {})
//                .flatMap(responseEntity -> {
//                    Page<ServiceProduct> services = new PageImpl<ServiceProduct>(responseEntity.getBody());
//                    return services;
//                })
                // .exchangeToMono(clientResponse -> clientResponse.toEntity(Resource.class))
                // .exchangeToMono(clientResponse -> clientResponse.toEntity(new ParameterizedTypeReference<PageImpl<ServiceProduct>>()))
                .onStatus(
                        HttpStatusCode::is4xxClientError,
                        response -> Mono.error(new RemoteServiceCallException("Сервис " + uri + " не найден", response.statusCode(), uri, response.body(BodyExtractors.toDataBuffers()).toString())))
                .onStatus(
                        HttpStatusCode::is5xxServerError,
                        response -> Mono.error(new RemoteServiceCallException("Сервис " + uri + " не доступен", response.statusCode(), uri, response.body(BodyExtractors.toDataBuffers()).toString())))
                .bodyToMono(new ParameterizedTypeReference<RestPage<ServiceProduct>>() {
                })
                // .bodyToMono(new PageImpl<ServiceProduct>(){})
//                .onErrorMap(WebClientRequestException.class, throwable -> new RemoteServiceCallException("Неизвестный url", HttpStatus.INTERNAL_SERVER_ERROR, uri, throwable.getMessage()))
//                .onErrorMap(Predicate.not(RemoteServiceCallException.class::isInstance),
//                        throwable -> new RemoteServiceCallException("Ошибка обработки данных при получении ответа", HttpStatus.BAD_REQUEST, uri, throwable.getMessage()))
//                .doOnError(err -> logger.error("Ошибка получения данных удаленного сервиса - {}", err.getMessage()))
                .log()
                .block();

    }

    public Page<ServiceVersionReadDto> findServiceVersionByFilters(DataFilter<ServiceVersionSearchCriteria> filter, Pageable pageable) {

        logger.info("Получение данных мэппинга услуг справочника КТ-777. Вызов сервиса НСИ с параметрами {}", filter);

        String uri = String.join("/", REFERENCE_SERVICE, "service-version/filter");

        return webClient
                .post()
                .uri(uri)
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
                .bodyToMono(new ParameterizedTypeReference<RestPage<ServiceVersionReadDto>>() {
                })
                .onErrorMap(WebClientRequestException.class, throwable -> new RemoteServiceCallException("Неизвестный url", HttpStatus.INTERNAL_SERVER_ERROR, uri, throwable.getMessage()))
                .onErrorMap(Predicate.not(RemoteServiceCallException.class::isInstance),
                        throwable -> new RemoteServiceCallException("Ошибка обработки данных при получении ответа", HttpStatus.BAD_REQUEST, uri, throwable.getMessage()))
                .doOnError(err -> logger.error("Ошибка получения данных удаленного сервиса - {}", err.getMessage()))
                .log()
                .block();
    }

    public ServiceVersionDTO changeServiceVersion(ServiceVersionDTO serviceVersion) {

        logger.info("Схранение/изменение мэппинга услуг справочника КТ-777. Вызов сервиса НСИ с параметрами {}", serviceVersion);

        String uri = String.join("/", REFERENCE_SERVICE, "service-version");

        return webClient
                .put()
                .uri(uri)
                .header(HttpHeaders.USER_AGENT, userAgent)
                .header(LoggingConstants.X_REQUEST_ID_HEADER, MDC.get(LoggingConstants.X_REQUEST_ID_KEY))
                .header(HttpHeaders.AUTHORIZATION, String.format("Bearer %s", userDetails.getJwtTokenValue()))
                .body(Mono.just(serviceVersion), ServiceVersionDTO.class)
                //.exchangeToMono(clientResponse -> clientResponse.toEntity(Resource.class))
                .exchangeToMono(clientResponse -> clientResponse.statusCode().equals(HttpStatus.OK) ?
                        clientResponse.bodyToMono(ServiceVersionDTO.class) :
                        clientResponse.createError())
                .log()
                .block();
    }

    public Long deleteServiceVersion(Long id) {

        logger.info("Удаление мэппинга услуг справочника КТ-777. Вызов сервиса НСИ с параметрами {}", id);

        String uri = String.join("/", REFERENCE_SERVICE, "service-version", id.toString());

        return webClient
                .delete()
                .uri(uri)
                .header(HttpHeaders.USER_AGENT, userAgent)
                .header(LoggingConstants.X_REQUEST_ID_HEADER, MDC.get(LoggingConstants.X_REQUEST_ID_KEY))
                .header(HttpHeaders.AUTHORIZATION, String.format("Bearer %s", userDetails.getJwtTokenValue()))
                //.body(Mono.just(), )
                .retrieve()
                .onStatus(
                        HttpStatusCode::is4xxClientError,
                        response -> Mono.error(new RemoteServiceCallException("Сервис " + uri + " не найден", response.statusCode(), uri, response.body(BodyExtractors.toDataBuffers()).toString())))
                .onStatus(
                        HttpStatusCode::is5xxServerError,
                        response -> Mono.error(new RemoteServiceCallException("Сервис " + uri + " не доступен", response.statusCode(), uri, response.body(BodyExtractors.toDataBuffers()).toString())))
                .bodyToMono(Long.class)
                .onErrorMap(WebClientRequestException.class, throwable -> new RemoteServiceCallException("Неизвестный url", HttpStatus.INTERNAL_SERVER_ERROR, uri, throwable.getMessage()))
                .onErrorMap(Predicate.not(RemoteServiceCallException.class::isInstance),
                        throwable -> new RemoteServiceCallException("Ошибка обработки данных при получении ответа", HttpStatus.BAD_REQUEST, uri, throwable.getMessage()))
                .doOnError(err -> logger.error("Ошибка получения данных удаленного сервиса - {}", err.getMessage()))
                .log()
                .block();
    }

    public ReferenceSavingResult uploadServiceVersion(List<ServiceVersionDTO> listServiceVersion) {

        logger.info("Загрузка мэппинга услуг справочника КТ-777. Вызов сервиса НСИ с параметрами {}", listServiceVersion);

        String uri = String.join("/", REFERENCE_SERVICE, "service-version");

        return webClient
                .post()
                .uri(uri)
                .header(HttpHeaders.USER_AGENT, userAgent)
                .header(LoggingConstants.X_REQUEST_ID_HEADER, MDC.get(LoggingConstants.X_REQUEST_ID_KEY))
                .header(HttpHeaders.AUTHORIZATION, String.format("Bearer %s", userDetails.getJwtTokenValue()))
                .body(Mono.just(listServiceVersion), List.class)
                .retrieve()
                .onStatus(
                        HttpStatusCode::is4xxClientError,
                        response -> Mono.error(new RemoteServiceCallException("Сервис " + uri + " не найден", response.statusCode(), uri, response.body(BodyExtractors.toDataBuffers()).toString())))
                .onStatus(
                        HttpStatusCode::is5xxServerError,
                        response -> Mono.error(new RemoteServiceCallException("Сервис " + uri + " не доступен", response.statusCode(), uri, response.body(BodyExtractors.toDataBuffers()).toString())))
                .bodyToMono(new ParameterizedTypeReference<ReferenceSavingResult>() {
                })
                .onErrorMap(WebClientRequestException.class, throwable -> new RemoteServiceCallException("Неизвестный url", HttpStatus.INTERNAL_SERVER_ERROR, uri, throwable.getMessage()))
                .onErrorMap(Predicate.not(RemoteServiceCallException.class::isInstance),
                        throwable -> new RemoteServiceCallException("Ошибка обработки данных при получении ответа", HttpStatus.BAD_REQUEST, uri, throwable.getMessage()))
                .doOnError(err -> logger.error("Ошибка получения данных удаленного сервиса - {}", err.getMessage()))
                .log()
                .block();
    }


    public Page<SegmentDto> getSegmentsByFilter(DataFilter<SegmentSearchCriteria> filter,Pageable pageable) {
        logger.info("Получение данных справочника сегментов. Вызов сервиса НСИ ");

        String uri = String.join("/", REFERENCE_SERVICE, "segment/filter");
        String urlTemplate = UriComponentsBuilder.fromUriString(uri)
                .queryParams(setPageableParams(pageable))
                .encode()
                .toUriString();

        return webClient
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
                .bodyToMono(new ParameterizedTypeReference<RestPage<SegmentDto>>() {
                })
                .onErrorMap(WebClientRequestException.class, throwable -> new RemoteServiceCallException("Неизвестный url", HttpStatus.INTERNAL_SERVER_ERROR, uri, throwable.getMessage()))
                .onErrorMap(Predicate.not(RemoteServiceCallException.class::isInstance),
                        throwable -> new RemoteServiceCallException("Ошибка обработки данных при получении ответа", HttpStatus.BAD_REQUEST, uri, throwable.getMessage()))
                .doOnError(err -> logger.error("Ошибка получения данных удаленного сервиса - {}", err.getMessage()))
                .log()
                .block();
    }

    public List<ServiceSegmentSubsegmentDto> findServiceSegmentSubsegmentsByServiceCode(String serviceCode, SegmentRecordType segmentRecordType) {
        logger.info("Получение данных справочника сегментов. Вызов сервиса НСИ ");

        String uri = String.join("/", REFERENCE_SERVICE, "service", serviceCode, String.valueOf(segmentRecordType));

        return webClient
                .get()
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
                .bodyToMono(new ParameterizedTypeReference<List<ServiceSegmentSubsegmentDto>>() {
                })
                .onErrorMap(WebClientRequestException.class, throwable -> new RemoteServiceCallException("Неизвестный url", HttpStatus.INTERNAL_SERVER_ERROR, uri, throwable.getMessage()))
                .onErrorMap(Predicate.not(RemoteServiceCallException.class::isInstance),
                        throwable -> new RemoteServiceCallException("Ошибка обработки данных при получении ответа", HttpStatus.BAD_REQUEST, uri, throwable.getMessage()))
                .doOnError(err -> logger.error("Ошибка получения данных удаленного сервиса - {}", err.getMessage()))
                .log()
                .block();

    }
    private  MultiValueMap<String, String> setPageableParams(Pageable pageable){
        MultiValueMap<String, String> result = new LinkedMultiValueMap<>();
        result.add("size",String.valueOf(pageable.getPageSize()));
        if (!pageable.getSort().isEmpty()){
            result.add("sort",pageable.getSort().toString().replace(": ",","));
        }
        result.add("offset",String.valueOf(pageable.getOffset()));
        return result;
    }
}
