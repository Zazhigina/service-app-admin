package igc.mirror.config;

import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
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

    @Value("${mirror.host}")
    private String baseURL;

    @Value("${mirror.endpoint.external.rbac}")
    private String rbacEndpoint;

    @Bean("rbac")
    public WebClient webClientRbac() throws SSLException {
        SslContext sslContext = SslContextBuilder
                .forClient()
                .trustManager(InsecureTrustManagerFactory.INSTANCE)
                .build();

        HttpClient httpClient = HttpClient.create().secure(sslContextSpec -> sslContextSpec.sslContext(sslContext));
        return WebClient.builder()
                .baseUrl(String.join("", baseURL, rbacEndpoint))
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .build();
    }
}
