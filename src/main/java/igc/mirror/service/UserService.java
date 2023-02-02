package igc.mirror.service;

import org.springframework.security.oauth2.jwt.Jwt;

import java.util.List;
import java.util.Optional;

public interface UserService {
    String getUsername();

    Optional<List<String>> getUserRoles(Jwt jwt);
}
