package igc.mirror.service.impl;

import igc.mirror.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    @Qualifier("rbac")
    private WebClient webClient;


    @Override
    public String getUsername() {
        if(SecurityContextHolder.getContext().getAuthentication() == null)
            return null;

        Jwt jwt =
                (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return jwt.getClaim("preferred_username");
    }

    /**
     * Получение списка ролей пользователя
     * {@linkplain //mirror.inlinegroup-c.ru/api/rbac}
     *
     * @param jwt Jwt токен
     * @return Список ролей
     */
    @Override
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
