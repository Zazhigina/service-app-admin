package igc.mirror.service;

import org.springframework.security.oauth2.jwt.Jwt;

import java.util.List;

public interface UserService {
    String getUsername();

    List<String> getUserRoles(Jwt jwt);
}
