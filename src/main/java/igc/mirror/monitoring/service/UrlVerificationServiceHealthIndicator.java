package igc.mirror.monitoring.service;

import igc.mirror.monitoring.repository.MonitoringRepository;
import igc.mirror.param.model.Param;
import igc.mirror.param.repository.ParamRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class UrlVerificationServiceHealthIndicator implements HealthIndicator {
    private final MonitoringRepository monitoringRepository;
    private final ParamRepository paramRepository;
    @Scheduled(cron = "0 0 * * * *")
    public void review() {
        try {
            if (!isHealthMonitoringEnabled()) {
                log.info("Проверка выключена");
                return;
            }

            log.info("Проверка включена. Началась проверка");
            health();
            log.info("Закончилась проверка");

        } catch (Exception e) {
            log.error("Ошибка при выполнении проверки: {}", e.getMessage(), e);
        }
    }


    public Health health() {
        Map<String, String> statuses = new HashMap<>();
        boolean allHealthy = true;

        List<String> services = monitoringRepository.getAllMonitoringUrls(); // Получаем URL из БД

        for (String service : services) {
            String status = checkService(service);
            statuses.put(service, status);
            if (!"UP".equals(status)) {
                allHealthy = false;
            }
        }

        return allHealthy ? Health.up().withDetails(statuses).build() : Health.down().withDetails(statuses).build();
    }

    private String checkService(String url) {
        String status;
        try {
            HttpURLConnection connection = (HttpURLConnection) (new URL(url)).openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);
            connection.connect();
            int responseCode = connection.getResponseCode();
            status = responseCode >= 200 && responseCode < 400 ? "UP" : "DOWN (Code: " + responseCode + ")";
        } catch (Exception var5) {
            status = "DOWN (Error: " + var5.getMessage() + ")";
        }

        this.monitoringRepository.updateStatus(url, status);
        return status;
    }

    private boolean isHealthMonitoringEnabled() {
        return Optional.ofNullable(paramRepository.find("HEALTH_MONITORING_ON"))
                .map(Param::getVal)
                .map(String::trim)
                .map(val -> val.equalsIgnoreCase("true"))
                .orElse(false);
    }


}