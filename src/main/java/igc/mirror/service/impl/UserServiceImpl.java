package igc.mirror.service.impl;

import igc.mirror.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Value("${mirror.services.rbac-base-url}")
    private String rbacBaseUrl;
    @Autowired
    RestTemplate restTemplate;

    @Override
    public String getUsername() {
        if(SecurityContextHolder.getContext().getAuthentication() == null)
            return null;

        Jwt jwt =
                (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return jwt.getClaim("preferred_username");
    }

    @Override
    public List<String> getUserRoles(Jwt jwt) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + jwt.getTokenValue());
        HttpEntity<Void> request = new HttpEntity<>(headers);

        ResponseEntity<List<String>> userRolesResponse = restTemplate.exchange(rbacBaseUrl + "user/roles", HttpMethod.GET, request, new ParameterizedTypeReference<List<String>>(){});
        return userRolesResponse.getBody();
    }
}
