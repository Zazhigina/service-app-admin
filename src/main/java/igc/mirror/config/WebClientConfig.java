package igc.mirror.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import javax.net.ssl.SSLException;

@Configuration
public class WebClientConfig {
    static final Logger logger = LoggerFactory.getLogger(WebClientConfig.class);

    @Value("${mirror.endpoint.rbac}")
    private String rbacBaseUrl;

    @Value("${mirror.endpoint.ma}")
    private String masterAssistantUrl;

    @Value("${mirror.keycloak.base-url}")
    private String keycloakBaseUrl;

    @Value("${mirror.endpoint.integration}")
    private String integrationUrl;

    @Value("${mirror.endpoint.doc}")
    private String docBaseUrl;

    @Bean("rbac")
    public WebClient webClientRbac(){
        HttpClient httpClient = HttpClient.create();
        return WebClient.builder()
                .baseUrl(rbacBaseUrl)
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .build();
    }

    @Bean("ma")
    public WebClient webClientMasterAssistant() {
        HttpClient httpClient = HttpClient.create();
        return WebClient.builder()
                .baseUrl(masterAssistantUrl)
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .build();
    }

    @Bean("keycloak")
    public WebClient webClientKeycloak() throws SSLException {
        HttpClient httpClient = HttpClient.create();
        return WebClient.builder()
                .baseUrl(keycloakBaseUrl)
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .build();
    }

    @Bean("integration")
    public WebClient webClientIntegration() throws SSLException {
        HttpClient httpClient = HttpClient.create();
        return WebClient.builder()
                .baseUrl(integrationUrl)
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .build();
    }

    @Bean("doc")
    public WebClient webClientDoc(){
        HttpClient httpClient = HttpClient.create();
        return WebClient.builder()
                .baseUrl(docBaseUrl)
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .build();
    }
}
