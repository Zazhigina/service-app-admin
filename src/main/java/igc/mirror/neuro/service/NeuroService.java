package igc.mirror.neuro.service;

import igc.mirror.auth.UserDetails;
import igc.mirror.config.LoggingConstants;
import igc.mirror.exception.common.RemoteServiceCallException;
import igc.mirror.neuro.dto.ElasticSearchOperations;
import igc.mirror.neuro.dto.NeuronetInfoDto;
import igc.mirror.neuro.dto.SystemConfigDto;
import igc.mirror.neuro.dto.SystemConfigFrontDto;
import igc.mirror.neuro.ref.ConfigType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Predicate;

@Service
@Validated
public class NeuroService {

    static final Logger logger = LoggerFactory.getLogger(NeuroService.class);

    @Value("${mirror.application.user-agent}")
    private String userAgent;

    @Autowired
    @Qualifier("pyEsDataMgmt")
    private WebClient webClientEsDataMgmt;

    @Autowired
    private UserDetails userDetails;

    public List<SystemConfigFrontDto> loadSystemConfig(ConfigType configType) {
        String uri = "/system-config?config_type=" + configType.getName();
        ResponseEntity<List<SystemConfigDto>> responseEntity =
                callEsDataMgmt(uri, HttpMethod.GET, new ParameterizedTypeReference<List<SystemConfigDto>>() {}, null);
        return responseEntity != null
                ? Objects.requireNonNull(responseEntity.getBody()).stream().map(SystemConfigDto::convertToSystemConfigFront).toList()
                : null;
    }

    public List<NeuronetInfoDto> loadListNeuronets() {
        String uri = "/neuronet";
        ResponseEntity<List<NeuronetInfoDto>> responseEntity =
                callEsDataMgmt(uri, HttpMethod.GET, new ParameterizedTypeReference<List<NeuronetInfoDto>>() {}, null);
        return responseEntity != null && responseEntity.getBody() != null ? responseEntity.getBody()
                : Collections.emptyList();
    }

    public ElasticSearchOperations buildVectors(String version) {
        String uri = "/build-vectors?version=" + version;
        ResponseEntity<ElasticSearchOperations> responseEntity =
                callEsDataMgmt(uri, HttpMethod.POST, new ParameterizedTypeReference<ElasticSearchOperations>() {}, null);
        return responseEntity != null && responseEntity.getBody() != null ? responseEntity.getBody() : null;
    }

    public ElasticSearchOperations activateSystemConfig(String version) {
        String uri = "/system-config/status/activate?version=" + version;
        ResponseEntity<ElasticSearchOperations> responseEntity =
                callEsDataMgmt(uri, HttpMethod.POST, new ParameterizedTypeReference<ElasticSearchOperations>() {}, null);
        return responseEntity != null ? responseEntity.getBody() : null;
    }

    public ElasticSearchOperations deleteSystemConfig(String version) {
        String uri = "/system-config?version=" + version;
        ResponseEntity<ElasticSearchOperations> responseEntity =
                callEsDataMgmt(uri, HttpMethod.DELETE, new ParameterizedTypeReference<ElasticSearchOperations>() {}, null);
        return responseEntity != null ? responseEntity.getBody() : null;
    }

    public ElasticSearchOperations addSystemConfig(SystemConfigDto systemConfigDto) {
        String uri = "/system-config";
        Map<String, Object> params = systemConfigDto.convertToPySchema();
        ResponseEntity<ElasticSearchOperations> responseEntity =
                callEsDataMgmt(uri, HttpMethod.PUT, new ParameterizedTypeReference<ElasticSearchOperations>() {}, params);
        return responseEntity != null ? responseEntity.getBody() : null;
    }

    public List<ElasticSearchOperations> deleteEsIndexes(String version) {
        String uri = "/indexes?version=" + version;
        ResponseEntity<List<ElasticSearchOperations>> responseEntity =
                callEsDataMgmt(uri, HttpMethod.DELETE, new ParameterizedTypeReference<List<ElasticSearchOperations>>() {}, null);
        return responseEntity != null ? responseEntity.getBody() : null;
    }

    private <T> ResponseEntity<T> callEsDataMgmt(String uri, HttpMethod method, ParameterizedTypeReference<T> responseType, Map<String, Object> requestBody) {
        WebClient.RequestBodySpec request = webClientEsDataMgmt
                .method(method)
                .uri(uri)
                .header(HttpHeaders.USER_AGENT, userAgent)
                .header(LoggingConstants.X_REQUEST_ID_HEADER, MDC.get(LoggingConstants.X_REQUEST_ID_KEY))
                .header(HttpHeaders.AUTHORIZATION, String.format("Bearer %s", userDetails.getJwtTokenValue()))
                .accept(MediaType.APPLICATION_JSON);

        if (requestBody != null) {
            request = (WebClient.RequestBodySpec) request.body(Mono.just(requestBody), Map.class);
        }

        return request.retrieve()
                .onStatus(
                        HttpStatusCode::is4xxClientError,
                        response -> response.bodyToMono(String.class)
                                .flatMap(error -> {
                                    logger.error(String.format("Ошибка получения данных Python с кодом %s: %s", response.statusCode(), error));
                                    return Mono.error(new RemoteServiceCallException("Ошибка получения данных Python 4xx", response.statusCode(), uri, error));
                                }))
                .onStatus(
                        HttpStatusCode::is5xxServerError,
                        response -> Mono.error(new RemoteServiceCallException("Сервис " + uri + " не доступен", response.statusCode(), uri, response.statusCode().toString())))
                .bodyToMono(responseType)
                .map(body -> ResponseEntity.ok().body(body))
                .onErrorMap(WebClientResponseException.class, throwable -> new RemoteServiceCallException("Неизвестный url", HttpStatus.INTERNAL_SERVER_ERROR, uri, throwable.getMessage()))
                .onErrorMap(Predicate.not(RemoteServiceCallException.class::isInstance),
                        throwable -> new RemoteServiceCallException("Ошибка обработки данных при получении ответа", HttpStatus.BAD_REQUEST, uri, throwable.getMessage()))
                .log()
                .block();
    }

}
