package igc.mirror.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;

@Component
public class UserHelper {
    static final Logger logger = LoggerFactory.getLogger(UserHelper.class);

    @Autowired
    @Qualifier("rbac")
    private WebClient webClient;

    /**
     * Возвращает имя авторизированного пользователя
     *
     * @return username
     */
    public Optional<String> getUsername() {
        if(SecurityContextHolder.getContext().getAuthentication() instanceof AnonymousAuthenticationToken ||
                !Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication()).isPresent())
            return Optional.empty();

        Jwt jwt =
                (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return Optional.ofNullable(jwt.getClaim("preferred_username"));
    }

    /**
     * Получение списка ролей пользователя
     *
     * @param jwt Jwt токен
     * @return Список ролей
     */
    public Optional<List<String>> getUserRoles(Jwt jwt) {
        return webClient
                .get()
                .uri("/user/roles")
                .header("Authorization", "Bearer " + jwt.getTokenValue())
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<String>>(){})
                .log()
                .onErrorResume(err -> {
                    logger.error("Ошибка запуска удаленного сервиса rbac - {}", err.getMessage());
                    return Mono.empty();
                })
                .blockOptional();
    }
}
