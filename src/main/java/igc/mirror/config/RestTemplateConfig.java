package igc.mirror.config;

import org.apache.http.client.HttpClient;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.SSLContext;

@Configuration
public class RestTemplateConfig {
    static final Logger logger = LoggerFactory.getLogger(RestTemplateConfig.class);
    @Value("${mirror.trust-store}")
    private Resource mirrorTrustStore;

    @Value("${mirror.trust-store-password}")
    private String mirrorTrustStorePassword;

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder){
        SSLContext sslContext = null;
        char[] trustStorePassword = null;

        if(!mirrorTrustStorePassword.isEmpty())
            trustStorePassword = mirrorTrustStorePassword.toCharArray();

        try {
            sslContext = SSLContextBuilder
                    .create()
                    .loadTrustMaterial(mirrorTrustStore.getFile(), trustStorePassword)
                    .build();
        }catch (Exception e){
            logger.error("Ошибка при подключении к keycloak, связанная с сертификатом", e.getCause());
        }

        SSLConnectionSocketFactory socketFactory = new SSLConnectionSocketFactory(sslContext);

        HttpClient httpClient = HttpClients.custom()
                .setSSLSocketFactory(socketFactory)
                .build();

        return builder
                .requestFactory(() -> new HttpComponentsClientHttpRequestFactory(httpClient))
                .build();
    }
}
