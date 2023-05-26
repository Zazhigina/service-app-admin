package igc.mirror.doc;

import igc.mirror.auth.UserDetails;
import igc.mirror.config.LoggingConstants;
import igc.mirror.doc.dto.DocumentDto;
import igc.mirror.exception.common.RemoteServiceCallException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.BodyExtractors;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import reactor.core.publisher.Mono;

import java.util.function.Predicate;

@Service
public class DocService {
    static final Logger logger = LoggerFactory.getLogger(DocService.class);

    @Value("${mirror.application.user-agent}")
    private String userAgent;

    private final WebClient webClient;
    private final UserDetails userDetails;

    @Autowired
    public DocService(@Qualifier("doc") WebClient webClient, UserDetails userDetails) {
        this.webClient = webClient;
        this.userDetails = userDetails;
    }

    public DocumentDto uploadDocument(MultipartFile multipartFile) {
        logger.info("Загрузка документа в сервис документов");

        String uri = "";

        MultipartBodyBuilder builder = new MultipartBodyBuilder();
        builder.part("file", multipartFile.getResource());

        return webClient
                .post()
                .uri(uri)
                .header(HttpHeaders.AUTHORIZATION, String.format("Bearer %s", userDetails.getJwtTokenValue()))
                .header(HttpHeaders.USER_AGENT, userAgent)
                .header(LoggingConstants.X_REQUEST_ID_HEADER, MDC.get(LoggingConstants.X_REQUEST_ID_KEY))
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(BodyInserters.fromMultipartData(builder.build()))
                .retrieve()
                .onStatus(
                    HttpStatusCode::is4xxClientError,
                    response -> response.bodyToMono(String.class)
                            .flatMap(error -> {
                                logger.error(String.format("Ошибка загрузки документов с кодом %s: %s", response.statusCode(), error));
                                return Mono.error(new RemoteServiceCallException("Ошибка вызова сервиса документаци 4xx", response.statusCode(), uri, response.body(BodyExtractors.toDataBuffers()).toString() ));
                }))
                .onStatus(
                        HttpStatusCode::is5xxServerError,
                        response -> Mono.error(new RemoteServiceCallException("Сервис " + uri + " не доступен", response.statusCode(), uri, response.body(BodyExtractors.toDataBuffers()).toString())))
                .bodyToMono(new ParameterizedTypeReference<DocumentDto>() {})
                .onErrorMap(WebClientRequestException.class, throwable -> new RemoteServiceCallException("Неизвестный url", HttpStatus.INTERNAL_SERVER_ERROR, uri, throwable.getMessage()))
                .onErrorMap(Predicate.not(RemoteServiceCallException.class::isInstance),
                        throwable -> new RemoteServiceCallException("Ошибка обработки данных при получении ответа", HttpStatus.BAD_REQUEST, uri, throwable.getMessage()))
                .doOnError(err -> logger.error("Ошибка получения данных удаленного сервиса - {}", err.getMessage()))
                .log()
                .block();
    }

    public DocumentDto downloadDocument(Long id) {
        logger.info("Выгрузка документа из сервиса документов");

        String uri = "/" + id;

        return
            webClient
            .get()
            .uri(uri)
            .header(HttpHeaders.AUTHORIZATION, String.format("Bearer %s", userDetails.getJwtTokenValue()))
            .header(HttpHeaders.USER_AGENT, userAgent)
            .header(LoggingConstants.X_REQUEST_ID_HEADER, MDC.get(LoggingConstants.X_REQUEST_ID_KEY))
            .retrieve()
            .onStatus(
                HttpStatusCode::is4xxClientError,
                response -> response.bodyToMono(String.class)
                        .flatMap(error -> {
                                logger.error(String.format("Ошибка загрузки документов с кодом %s: %s", response.statusCode(), error));
                                return Mono.error(new RemoteServiceCallException("Ошибка вызова сервиса документаци 4xx", response.statusCode(), uri, response.body(BodyExtractors.toDataBuffers()).toString() ));
            }))
            .onStatus(
                    HttpStatusCode::is5xxServerError,
                    response -> Mono.error(new RemoteServiceCallException("Сервис " + uri + " не доступен", response.statusCode(), uri, response.body(BodyExtractors.toDataBuffers()).toString())))
            .toEntity(new ParameterizedTypeReference<Resource>() {})
            .flatMap(responseEntity -> {
                DocumentDto document = new DocumentDto();
                document.setResource(responseEntity.getBody());
                document.setContentDisposition(responseEntity.getHeaders().getContentDisposition());
                return Mono.just(document);
            })
            .onErrorMap(WebClientRequestException.class, throwable -> new RemoteServiceCallException("Неизвестный url", HttpStatus.INTERNAL_SERVER_ERROR, uri, throwable.getMessage()))
            .onErrorMap(Predicate.not(RemoteServiceCallException.class::isInstance),
                    throwable -> new RemoteServiceCallException("Ошибка обработки данных при получении ответа", HttpStatus.BAD_REQUEST, uri, throwable.getMessage()))
            .doOnError(err -> logger.error("Ошибка получения данных удаленного сервиса - {}", err.getMessage()))
            .log()
            .block();
    }

    public DocumentDto changeUploadedDocument(Long id, MultipartFile multipartFile){
        logger.info("Перезапись документа с ID - {}", id);

        String uri = "/" + id;

        MultipartBodyBuilder builder = new MultipartBodyBuilder();
        builder.part("file", multipartFile.getResource());

        return webClient
                .put()
                .uri(uri)
                .header(HttpHeaders.AUTHORIZATION, String.format("Bearer %s", userDetails.getJwtTokenValue()))
                .header(HttpHeaders.USER_AGENT, userAgent)
                .header(LoggingConstants.X_REQUEST_ID_HEADER, MDC.get(LoggingConstants.X_REQUEST_ID_KEY))
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(BodyInserters.fromMultipartData(builder.build()))
                .retrieve()
                .onStatus(
                    HttpStatusCode::is4xxClientError,
                    response -> response.bodyToMono(String.class)
                            .flatMap(error -> {
                                logger.error(String.format("Ошибка загрузки документов с кодом %s: %s", response.statusCode(), error));
                                return Mono.error(new RemoteServiceCallException("Ошибка вызова сервиса документаци 4xx", response.statusCode(), uri, response.body(BodyExtractors.toDataBuffers()).toString() ));
                }))
                .onStatus(
                        HttpStatusCode::is5xxServerError,
                        response -> Mono.error(new RemoteServiceCallException("Сервис " + uri + " не доступен", response.statusCode(), uri, response.body(BodyExtractors.toDataBuffers()).toString())))
                .bodyToMono(new ParameterizedTypeReference<DocumentDto>() {})
                .onErrorMap(WebClientRequestException.class, throwable -> new RemoteServiceCallException("Неизвестный url", HttpStatus.INTERNAL_SERVER_ERROR, uri, throwable.getMessage()))
                .onErrorMap(Predicate.not(RemoteServiceCallException.class::isInstance),
                        throwable -> new RemoteServiceCallException("Ошибка обработки данных при получении ответа", HttpStatus.BAD_REQUEST, uri, throwable.getMessage()))
                .doOnError(err -> logger.error("Ошибка получения данных удаленного сервиса - {}", err.getMessage()))
                .log()
                .block();
    }

    public void deleteUploadedDocument(Long id){
        logger.info("Удаление документа с ID - {}", id);

        String uri = "/" + id;

        webClient
                .delete()
                .uri(uri)
                .header(HttpHeaders.AUTHORIZATION, String.format("Bearer %s", userDetails.getJwtTokenValue()))
                .header(HttpHeaders.USER_AGENT, userAgent)
                .header(LoggingConstants.X_REQUEST_ID_HEADER, MDC.get(LoggingConstants.X_REQUEST_ID_KEY))
                .retrieve()
                .onStatus(
                        HttpStatusCode::is4xxClientError,
                        response -> response.bodyToMono(String.class)
                                .flatMap(error -> {
                                    logger.error(String.format("Ошибка загрузки документов с кодом %s: %s", response.statusCode(), error));
                                    return Mono.error(new RemoteServiceCallException("Ошибка вызова сервиса документаци 4xx", response.statusCode(), uri, response.body(BodyExtractors.toDataBuffers()).toString() ));
                                }))
                .onStatus(
                        HttpStatusCode::is5xxServerError,
                        response -> Mono.error(new RemoteServiceCallException("Сервис " + uri + " не доступен", response.statusCode(), uri, response.body(BodyExtractors.toDataBuffers()).toString())))
                .bodyToMono(new ParameterizedTypeReference<DocumentDto>() {})
                .onErrorMap(WebClientRequestException.class, throwable -> new RemoteServiceCallException("Неизвестный url", HttpStatus.INTERNAL_SERVER_ERROR, uri, throwable.getMessage()))
                .onErrorMap(Predicate.not(RemoteServiceCallException.class::isInstance),
                        throwable -> new RemoteServiceCallException("Ошибка обработки данных при получении ответа", HttpStatus.BAD_REQUEST, uri, throwable.getMessage()))
                .doOnError(err -> logger.error("Ошибка получения данных удаленного сервиса - {}", err.getMessage()))
                .log()
                .block();
    }

    public DocumentDto retrieveDocumentInfo(Long id){
        String uri = "/" + id + "/info";

        return webClient
                .get()
                .uri(uri)
                .header(HttpHeaders.AUTHORIZATION, String.format("Bearer %s", userDetails.getJwtTokenValue()))
                .header(HttpHeaders.USER_AGENT, userAgent)
                .header(LoggingConstants.X_REQUEST_ID_HEADER, MDC.get(LoggingConstants.X_REQUEST_ID_KEY))
                .retrieve()
                .onStatus(
                        HttpStatusCode::is4xxClientError,
                        response -> response.bodyToMono(String.class)
                                .flatMap(error -> {
                                    logger.error(String.format("Ошибка получения данных документа %s: %s", response.statusCode(), error));
                                    return Mono.error(new RemoteServiceCallException("Ошибка вызова сервиса документаци 4xx", response.statusCode(), uri, response.body(BodyExtractors.toDataBuffers()).toString() ));
                                }))
                .onStatus(
                        HttpStatusCode::is5xxServerError,
                        response -> Mono.error(new RemoteServiceCallException("Сервис " + uri + " не доступен", response.statusCode(), uri, response.body(BodyExtractors.toDataBuffers()).toString())))
                .bodyToMono(new ParameterizedTypeReference<DocumentDto>() {})
                .onErrorMap(WebClientRequestException.class, throwable -> new RemoteServiceCallException("Неизвестный url", HttpStatus.INTERNAL_SERVER_ERROR, uri, throwable.getMessage()))
                .onErrorMap(Predicate.not(RemoteServiceCallException.class::isInstance),
                        throwable -> new RemoteServiceCallException("Ошибка обработки данных при получении ответа", HttpStatus.BAD_REQUEST, uri, throwable.getMessage()))
                .doOnError(err -> logger.error("Ошибка получения данных удаленного сервиса - {}", err.getMessage()))
                .log()
                .block();
    }

}
