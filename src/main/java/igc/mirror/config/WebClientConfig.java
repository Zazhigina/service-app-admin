package igc.mirror.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

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

    @Value("${mirror.endpoint.ep}")
    private String ePriceUrl;

    @Value("${mirror.endpoint.nsi}")
    private String nsiUrl;

    @Value("${mirror.endpoint.report}")
    private String reportUrl;

    @Value("${mirror.endpoint.chat}")
    private String chatUrl;

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
    public WebClient webClientKeycloak() {
        HttpClient httpClient = HttpClient.create();
        return WebClient.builder()
                .baseUrl(keycloakBaseUrl)
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .build();
    }

    @Bean("integration")
    public WebClient webClientIntegration() {
        HttpClient httpClient = HttpClient.create();
        return WebClient.builder()
                .baseUrl(integrationUrl)
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .build();
    }

    @Bean("doc")
    public WebClient webClientDoc() {
        HttpClient httpClient = HttpClient.create();
        return WebClient.builder()
                .baseUrl(docBaseUrl)
                .exchangeStrategies(ExchangeStrategies.builder()
                        .codecs(configurer ->
                                configurer.defaultCodecs()
                                        .maxInMemorySize(20 * 1024 * 1024)
                        )
                        .build())
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .build();
    }

    @Bean("ep")
    public WebClient webClientEPrice() {
        HttpClient httpClient = HttpClient.create();
        return WebClient.builder()
                .baseUrl(ePriceUrl)
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .build();
    }
    @Bean("nsi")
    public WebClient webClientNSI() {
        HttpClient httpClient = HttpClient.create();
        return WebClient.builder()
                .baseUrl(nsiUrl)
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .build();
    }

    @Bean("report")
    public WebClient webClientReport() {
        HttpClient httpClient = HttpClient.create();
        return WebClient.builder()
                .baseUrl(reportUrl)
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .build();
    }

    @Bean("chat")
    public WebClient webClientChat() {
        HttpClient httpClient = HttpClient.create();
        return WebClient.builder()
                .baseUrl(chatUrl)
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .build();
    }
}
