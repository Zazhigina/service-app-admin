package igc.mirror.config;

import com.nimbusds.jose.shaded.json.JSONArray;
import igc.mirror.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.web.client.RestTemplate;

import java.security.interfaces.RSAPublicKey;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig {
    static final Logger logger = LoggerFactory.getLogger(SecurityConfig.class);
    private static final String PUBLIC_ENDPOINT = "/public/**";
    private static final String[] API_ENDPOINT = {
        "/param/**"
    };

    @Value("${keycloak.jwk-set-uri}")
    private String keycloakJwkSetUri;
    @Value("${keycloak.public-key}")
    private RSAPublicKey jwtPublicKey;
    @Value("${keycloak.with-public-key}")
    private Boolean keycloakWithPublicKey;
    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private UserService userService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .exceptionHandling()
                .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
                .and()
                .httpBasic().disable()
                .csrf().disable()
                .oauth2ResourceServer(resourceServerConfigurer -> resourceServerConfigurer
                        .jwt(jwtConfigurer -> jwtConfigurer
                                .jwtAuthenticationConverter(jwtAuthenticationConverter()))
                .and())
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers(PUBLIC_ENDPOINT,
                        "/v3/api-docs/**", "/actuator/**" ).permitAll()
                .antMatchers(API_ENDPOINT).hasRole("APP_ADMIN.EXEC")
                .anyRequest().authenticated();

        return http.build();
    }

    @Bean
    public Converter<Jwt, AbstractAuthenticationToken> jwtAuthenticationConverter() {
        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter());
        return jwtAuthenticationConverter;
    }

    @Bean
    public Converter<Jwt, Collection<GrantedAuthority>> jwtGrantedAuthoritiesConverter() {
        JwtGrantedAuthoritiesConverter delegate = new JwtGrantedAuthoritiesConverter();
        return new Converter<>() {
            @Override
            public Collection<GrantedAuthority> convert(Jwt jwt) {
                Collection<GrantedAuthority> grantedAuthorities = new ArrayList<>();

                JSONArray groups = jwt.getClaim("groups");
//                if (groups != null)
//                    groups.forEach(group -> {
//                        grantedAuthorities.add(new SimpleGrantedAuthority("GROUP_" + group.toString()));
//                    });

                List<String> userRoles = userService.getUserRoles(jwt);

//                if (groups.contains(InitAdmGroup.MIRROR_BASE.getGroup())) {
////                    List<String> roles = groupService.getRolesByGroupNames(groups.stream().map(group -> group.toString()).collect(Collectors.toList()));
//                    List<String> roles = userService.getUserRoles(groups.stream().map(group -> group.toString()).collect(Collectors.toList()));
                    final List<SimpleGrantedAuthority> keycloakAuthorities = userRoles.stream().map(role -> new SimpleGrantedAuthority("ROLE_" + role)).collect(Collectors.toList());
                    grantedAuthorities.addAll(keycloakAuthorities);
//                }

                return grantedAuthorities;
            }
        };
    }

    @Bean
    public JwtDecoder jwtDecoder() {
        if(keycloakWithPublicKey)
            return NimbusJwtDecoder.withPublicKey(jwtPublicKey).build();
        return NimbusJwtDecoder.withJwkSetUri(keycloakJwkSetUri).restOperations(restTemplate).build();
    }

}
