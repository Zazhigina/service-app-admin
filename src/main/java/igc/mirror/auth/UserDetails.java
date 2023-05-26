package igc.mirror.auth;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;

@Component
public class UserDetails {
    static final Logger logger = LoggerFactory.getLogger(UserDetails.class);

    @Autowired
    @Qualifier("rbac")
    private WebClient webClient;

    /**
     * Возвращает имя авторизированного пользователя
     * В случае, если endpoint включен в разрешенные(e.g. permitAll()) - метод вернет null
     * @return username
     */
    public String getUsername() {
        try{
            Jwt jwt =
                    (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            return jwt.getClaim("preferred_username");
        }catch (Exception e){
            return null;
        }
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

    /**
     * Возвращает значение JWT access токен'а
     *
     * @return jwt access token
     */
    public String getJwtTokenValue(){
        Jwt jwt =
                (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return jwt.getTokenValue();

    }
}
