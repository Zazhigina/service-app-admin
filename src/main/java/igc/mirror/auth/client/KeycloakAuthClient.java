package igc.mirror.auth.client;

import igc.mirror.auth.dto.AuthResponseDto;
import igc.mirror.config.LoggingConstants;
import igc.mirror.exception.common.RemoteServiceCallException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyExtractors;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import reactor.core.publisher.Mono;

import java.util.function.Predicate;

@Component
public class KeycloakAuthClient {
    private static final Logger logger = LoggerFactory.getLogger(KeycloakAuthClient.class);

    private static final String GRANT_TYPE = "grant_type";
    private static final String CLIENT_ID = "client_id";
    private static final String CLIENT_SECRET = "client_secret";
    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";

    @Value("${mirror.keycloak.realm}")
    private String realm;

    @Value("${mirror.keycloak.client-id}")
    private String clientId;

    @Value("${mirror.keycloak.client-secret}")
    private String clientSecret;

    @Value("${mirror.application.user-agent}")
    private String userAgent;

    @Autowired
    @Qualifier("keycloak")
    private WebClient webClient;

    public AuthResponseDto authenticate(String username, String password) {
        logger.info("Вызов keycloak для получения JWT токена");

        String authPath = String.format("/realms/%s/protocol/openid-connect/token", realm);

        MultiValueMap<String, String> paramMap = new LinkedMultiValueMap<>();

        paramMap.add(GRANT_TYPE, PASSWORD);
        paramMap.add(CLIENT_ID, clientId);
        paramMap.add(CLIENT_SECRET, clientSecret);
        paramMap.add(USERNAME, username);
        paramMap.add(PASSWORD, password);

        return webClient
                .post()
                .uri(authPath)
                .header(HttpHeaders.USER_AGENT, userAgent)
                .header(LoggingConstants.X_REQUEST_ID_HEADER, MDC.get(LoggingConstants.X_REQUEST_ID_KEY))
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData(paramMap))
                .retrieve()
                .onStatus(
                        HttpStatusCode::is4xxClientError,
                        response -> response.bodyToMono(String.class)
                                .flatMap(error -> Mono.error(new RemoteServiceCallException("Ошибка аутентификации", response.statusCode(), authPath, error )))
                )
                .onStatus(
                        HttpStatusCode::is5xxServerError,
                        response -> Mono.error(new RemoteServiceCallException("Keycloak недоступен", response.statusCode(), authPath, response.body(BodyExtractors.toDataBuffers()).toString())))
                .bodyToMono(new ParameterizedTypeReference<AuthResponseDto>(){})
                .onErrorMap(WebClientRequestException.class, throwable -> new RemoteServiceCallException("Неизвестный url", HttpStatus.INTERNAL_SERVER_ERROR, authPath, throwable.getMessage()))
                .onErrorMap(Predicate.not(RemoteServiceCallException.class::isInstance),
                        throwable -> new RemoteServiceCallException("Ошибка обработки данных при получении ответа", HttpStatus.BAD_REQUEST, authPath, throwable.getMessage()))
                .doOnError(error -> logger.error("Ошибка вызова keycloak - {}", error.getMessage()))
                .doOnSuccess(success -> logger.info("Получен JWT токен от keycloak"))
                .log()
                .block();
    }
}
