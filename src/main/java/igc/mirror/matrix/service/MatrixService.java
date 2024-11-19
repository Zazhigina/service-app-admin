package igc.mirror.matrix.service;

import igc.mirror.auth.UserDetails;
import igc.mirror.config.LoggingConstants;
import igc.mirror.exception.common.RemoteServiceCallException;
import igc.mirror.matrix.dto.MatrixDTO;
import igc.mirror.matrix.filter.InitiatorSearchCriteria;
import igc.mirror.matrix.filter.MatrixSearchCriteria;
import igc.mirror.matrix.filter.OrgSearchCriteria;
import igc.mirror.matrix.repository.MatrixRepository;
import igc.mirror.matrix.model.Customer;
import igc.mirror.matrix.model.Initiator;
import igc.mirror.matrix.model.Matrix;
import igc.mirror.matrix.model.Org;
import igc.mirror.service.dto.RestPage;
import igc.mirror.utils.qfilter.DataFilter;
import igc.mirror.utils.web.WebServiceUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.reactive.function.BodyExtractors;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Service
@Validated
public class MatrixService {
    static final Logger logger = LoggerFactory.getLogger(MatrixService.class);
    private final UserDetails userDetails;
    @Value("${mirror.application.user-agent}")
    private String userAgent;
    private final WebClient webClient;
    private final MatrixRepository matrixRepository;
    @Autowired
    private WebServiceUtil webServiceUtil;
    static final String REFERENCE_SERVICE = "/reference";

    public MatrixService(UserDetails userDetails, @Qualifier("nsi") WebClient webClient,
                         MatrixRepository matrixRepository
    ) {

        this.userDetails = userDetails;
        this.webClient = webClient;
        this.matrixRepository = matrixRepository;
    }

    /**
     * Возвращает список заказчиков по списку кодов
     * @return список заказчиков по списку кодов
     */
    private Map<String, Customer> getCustomerByCodes(List<String> codes) {
        logger.info("Получение заказчиков по кодам");

        String uri = String.join("/", REFERENCE_SERVICE, "customer/map");

        return webClient
                .post()
                .uri(uri)
                .header(HttpHeaders.CONTENT_TYPE, APPLICATION_JSON_VALUE)
                .header(HttpHeaders.USER_AGENT, userAgent)
                .header(LoggingConstants.X_REQUEST_ID_HEADER, MDC.get(LoggingConstants.X_REQUEST_ID_KEY))
                .header(HttpHeaders.AUTHORIZATION, String.format("Bearer %s", userDetails.getJwtTokenValue()))
                .accept(MediaType.APPLICATION_JSON)
                .acceptCharset(StandardCharsets.UTF_8)
                .body(Mono.just(codes), List.class)
                .retrieve()
                .onStatus(
                        HttpStatusCode::is4xxClientError,
                        response -> Mono.error(new RemoteServiceCallException("Сервис " + uri + " не найден", response.statusCode(), uri, response.body(BodyExtractors.toDataBuffers()).toString())))
                .onStatus(
                        HttpStatusCode::is5xxServerError,
                        response -> Mono.error(new RemoteServiceCallException("Сервис " + uri + " не доступен", response.statusCode(), uri, response.body(BodyExtractors.toDataBuffers()).toString())))
                .bodyToMono(new ParameterizedTypeReference<Map<String, Customer>>() { })
                .log()
                .onErrorMap(WebClientRequestException.class, throwable -> new RemoteServiceCallException("Неизвестный url", HttpStatus.INTERNAL_SERVER_ERROR, uri, throwable.getMessage()))
                .onErrorMap(Predicate.not(RemoteServiceCallException.class::isInstance),
                        throwable -> new RemoteServiceCallException("Ошибка обработки данных при получении ответа", HttpStatus.BAD_REQUEST, uri, throwable.getMessage()))
                .doOnError(err -> logger.error("Ошибка запуска удаленного сервиса - {}", err.getMessage()))
                .block();

    }

    /**
     * Возвращает список заказчиков по заданному фильтру
     *
     * @param dataFilter фильтр по заказчикам
     * @return список заказчиков
     */
    private RestPage<Customer> getCustomerByFilter(DataFilter<MatrixSearchCriteria> dataFilter, Pageable pageable) {
        logger.info("Получение данных заказчиков с фильтром");

        String uri = String.join("/", REFERENCE_SERVICE, "customer/filter");

        String urlTemplate = webServiceUtil.buildUriByPageableProperties(uri, PageRequest.of(pageable.getPageNumber(), pageable.getPageSize()));

        return webClient
                .post()
                .uri(urlTemplate)
                .header(HttpHeaders.CONTENT_TYPE, APPLICATION_JSON_VALUE)
                .header(HttpHeaders.USER_AGENT, userAgent)
                .header(LoggingConstants.X_REQUEST_ID_HEADER, MDC.get(LoggingConstants.X_REQUEST_ID_KEY))
                .header(HttpHeaders.AUTHORIZATION, String.format("Bearer %s", userDetails.getJwtTokenValue()))
                .accept(MediaType.APPLICATION_JSON)
                .acceptCharset(StandardCharsets.UTF_8)
                .body(Mono.just(dataFilter), DataFilter.class)
                .retrieve()
                .onStatus(
                        HttpStatusCode::is4xxClientError,
                        response -> Mono.error(new RemoteServiceCallException("Сервис " + uri + " не найден", response.statusCode(), uri, response.body(BodyExtractors.toDataBuffers()).toString())))
                .onStatus(
                        HttpStatusCode::is5xxServerError,
                        response -> Mono.error(new RemoteServiceCallException("Сервис " + uri + " не доступен", response.statusCode(), uri, response.body(BodyExtractors.toDataBuffers()).toString())))
                .bodyToMono(new ParameterizedTypeReference<RestPage<Customer>>() { })
                .log()
                .onErrorMap(WebClientRequestException.class, throwable -> new RemoteServiceCallException("Неизвестный url", HttpStatus.INTERNAL_SERVER_ERROR, uri, throwable.getMessage()))
                .onErrorMap(Predicate.not(RemoteServiceCallException.class::isInstance),
                        throwable -> new RemoteServiceCallException("Ошибка обработки данных при получении ответа", HttpStatus.BAD_REQUEST, uri, throwable.getMessage()))
                .doOnError(err -> logger.error("Ошибка запуска удаленного сервиса - {}", err.getMessage()))
                .block();

    }

    /**
     * Возвращает список инициаторов по заданному фильтру
     *
     * @param dataFilter фильтр по инициаторам
     * @return список инициаторов
     */
    private RestPage<Initiator> getInitiatorByFilter(DataFilter<InitiatorSearchCriteria> dataFilter, Pageable pageable) {
        logger.info("Получение данных инициаторов с фильтром");

        String uri = String.join("/", REFERENCE_SERVICE, "initiator/filter");

        String urlTemplate = webServiceUtil.buildUriByPageableProperties(uri, PageRequest.of(pageable.getPageNumber(), pageable.getPageSize()));

        //dataFilter.getSearchCriteria() == "orgCo"

        return webClient
                .post()
                .uri(urlTemplate)
                .header(HttpHeaders.CONTENT_TYPE, APPLICATION_JSON_VALUE)
                .header(HttpHeaders.USER_AGENT, userAgent)
                .header(LoggingConstants.X_REQUEST_ID_HEADER, MDC.get(LoggingConstants.X_REQUEST_ID_KEY))
                .header(HttpHeaders.AUTHORIZATION, String.format("Bearer %s", userDetails.getJwtTokenValue()))
                .accept(MediaType.APPLICATION_JSON)
                .acceptCharset(StandardCharsets.UTF_8)
                .body(Mono.just(dataFilter), DataFilter.class)
                .retrieve()
                .onStatus(
                        HttpStatusCode::is4xxClientError,
                        response -> Mono.error(new RemoteServiceCallException("Сервис " + uri + " не найден", response.statusCode(), uri, response.body(BodyExtractors.toDataBuffers()).toString())))
                .onStatus(
                        HttpStatusCode::is5xxServerError,
                        response -> Mono.error(new RemoteServiceCallException("Сервис " + uri + " не доступен", response.statusCode(), uri, response.body(BodyExtractors.toDataBuffers()).toString())))
                .bodyToMono(new ParameterizedTypeReference<RestPage<Initiator>>() { })
                .log()
                .onErrorMap(WebClientRequestException.class, throwable -> new RemoteServiceCallException("Неизвестный url", HttpStatus.INTERNAL_SERVER_ERROR, uri, throwable.getMessage()))
                .onErrorMap(Predicate.not(RemoteServiceCallException.class::isInstance),
                        throwable -> new RemoteServiceCallException("Ошибка обработки данных при получении ответа", HttpStatus.BAD_REQUEST, uri, throwable.getMessage()))
                .doOnError(err -> logger.error("Ошибка запуска удаленного сервиса - {}", err.getMessage()))
                .block();

    }

    /**
     * Возвращает список организаторов по заданному фильтру
     *
     * @param dataFilter фильтр по организаторам
     * @return список организаторов
     */
    private RestPage<Org> getOrgByFilter(DataFilter<OrgSearchCriteria> dataFilter, Pageable pageable) {
        logger.info("Получение данных организаторов с фильтром");

        String uri = String.join("/", REFERENCE_SERVICE, "org/filter");
        String urlTemplate = webServiceUtil.buildUriByPageableProperties(uri, PageRequest.of(pageable.getPageNumber(), pageable.getPageSize()));

        return webClient
                .post()
                .uri(urlTemplate)
                .header(HttpHeaders.CONTENT_TYPE, APPLICATION_JSON_VALUE)
                .header(HttpHeaders.USER_AGENT, userAgent)
                .header(LoggingConstants.X_REQUEST_ID_HEADER, MDC.get(LoggingConstants.X_REQUEST_ID_KEY))
                .header(HttpHeaders.AUTHORIZATION, String.format("Bearer %s", userDetails.getJwtTokenValue()))
                .accept(MediaType.APPLICATION_JSON)
                .acceptCharset(StandardCharsets.UTF_8)
                .body(Mono.just(dataFilter), DataFilter.class)
                .retrieve()
                .onStatus(
                        HttpStatusCode::is4xxClientError,
                        response -> Mono.error(new RemoteServiceCallException("Сервис " + uri + " не найден", response.statusCode(), uri, response.body(BodyExtractors.toDataBuffers()).toString())))
                .onStatus(
                        HttpStatusCode::is5xxServerError,
                        response -> Mono.error(new RemoteServiceCallException("Сервис " + uri + " не доступен", response.statusCode(), uri, response.body(BodyExtractors.toDataBuffers()).toString())))
                .bodyToMono(new ParameterizedTypeReference<RestPage<Org>>() { })
                .log()
                .onErrorMap(WebClientRequestException.class, throwable -> new RemoteServiceCallException("Неизвестный url", HttpStatus.INTERNAL_SERVER_ERROR, uri, throwable.getMessage()))
                .onErrorMap(Predicate.not(RemoteServiceCallException.class::isInstance),
                        throwable -> new RemoteServiceCallException("Ошибка обработки данных при получении ответа", HttpStatus.BAD_REQUEST, uri, throwable.getMessage()))
                .doOnError(err -> logger.error("Ошибка запуска удаленного сервиса - {}", err.getMessage()))
                .block();

    }

    /**
     * Возвращает массив заказчиков по кодам
     *
     * @param codes фильтр заказчиков
     * @return карта соответствия заказчиков
     */
    public List<Customer> getCustomerByCodesAsList(List<String> codes) {
        Map<String,Customer> customers = getCustomerByCodes(codes);

        List<Customer> customersList = new ArrayList<>(customers.values());

        return customersList;
    }

    /**
     * Возвращает карту соответствия заказчиков
     *
     * @param dataFilter фильтр заказчиков
     * @return карта соответствия заказчиков
     */
    public List<Customer> getCustomerByFilterAsList(DataFilter<MatrixSearchCriteria> dataFilter, Pageable pageable) {
        RestPage<Customer> customers = getCustomerByFilter(dataFilter, pageable);

        return customers.stream().collect(Collectors.toList());
    }

    /**
     * Возвращает карту соответствия инициаторов
     *
     * @param dataFilter фильтр инициатора
     * @return карта соответствия инициаторов
     */
    public List<Initiator> getInitiatorByFilterAsList(DataFilter<InitiatorSearchCriteria> dataFilter, Pageable pageable) {
        RestPage<Initiator> initiators = getInitiatorByFilter(dataFilter, pageable);

        return initiators.stream().collect(Collectors.toList());
    }

    /**
     * Возвращает карту соответствия организаторов
     *
     * @param dataFilter фильтр организаторов
     * @return карта соответствия организаторов
     */
    public List<Org> getOrgByFilterAsList(DataFilter<OrgSearchCriteria> dataFilter, Pageable pageable) {
        RestPage<Org> orgs = getOrgByFilter(dataFilter, pageable);

        return orgs.stream().collect(Collectors.toList());
    }

    /**
     * Получить данные из Матрицы Компания организатор / Организатор - Заказчик / Инициатор
     */

    public List<Matrix> getMatrixInfo(){
        List<Matrix> matrixObjects = matrixRepository.getAllObjects();

        List<Matrix> matrix = new ArrayList<>(matrixObjects);
        return matrix;
    }

    /**
     * Сохранение данных в Матрицу Компания организатор / Организатор - Заказчик / Инициатор
     *
     * @param matrixDTO данных в Матрицу Компания организатор / Организатор - Заказчик / Инициатор
     */
    public void addMatrixInfo(List<MatrixDTO> matrixDTO) {

        for (MatrixDTO matrix_object : matrixDTO) {

            var matrixInfo = matrixRepository.getMatrixInfoByAllParams(matrix_object.getCompanyCode(), matrix_object.getOrgCode(), matrix_object.getCustomerCode(), matrix_object.getInitiatorCode());

            if (matrixInfo.isEmpty()){
                Matrix matrixFinalObj = new Matrix();
                matrixFinalObj.setCompanyCode(matrix_object.getCompanyCode());
                matrixFinalObj.setOrgCode(matrix_object.getOrgCode());
                matrixFinalObj.setCustomerCode(matrix_object.getCustomerCode());
                matrixFinalObj.setInitiatorCode(matrix_object.getInitiatorCode());
                matrixFinalObj.fillAuthInfo(userDetails.getUsername());
                matrixRepository.insertMatrixInfo(matrixFinalObj);
            } else {
                continue;
            }
        }
    }

    /**
     * Удалить компанию организатора и её связи из Матрицы Компания организатор / Организатор - Заказчик / Инициатор
     */
    public void deleteInfoByCompanyCode(String companyCode) {

        var matrix_obj = matrixRepository.getInfoByCompanyCode(companyCode);

        if (!matrix_obj.isEmpty()){
            matrixRepository.deleteByCompanyCode(companyCode);
        }
    }
    /**
     * Удалить заказчика/инициатора из Матрицы Компания организатор / Организатор - Заказчик / Инициатор
     */
    public void deleteInfoByObject(List<MatrixDTO> matrixDTO) {
        for (MatrixDTO matrix_object : matrixDTO) {

            var matrixInfo = matrixRepository.getMatrixInfoByAllParams(matrix_object.getCompanyCode(), matrix_object.getOrgCode(), matrix_object.getCustomerCode(), matrix_object.getInitiatorCode());

            if (!matrixInfo.isEmpty()){

                matrixRepository.deleteByObject(matrix_object.getCompanyCode(), matrix_object.getOrgCode(), matrix_object.getCustomerCode(), matrix_object.getInitiatorCode());

            } else {
                continue;
            }
        }
    }
}
