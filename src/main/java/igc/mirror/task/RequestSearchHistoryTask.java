package igc.mirror.task;

import igc.mirror.auth.client.KeycloakAuthClient;
import igc.mirror.auth.dto.AuthResponseDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.reactive.function.client.WebClient;

public class RequestSearchHistoryTask {
    static final Logger logger = LoggerFactory.getLogger(RequestSearchHistoryTask.class);

    @Autowired
    @Qualifier("ma")
    private WebClient webClient;

    @Autowired
    private KeycloakAuthClient keycloakAuthClient;

    @Value("${mirror.schedule.tasks.username}")
    private String scheduleTasksUserName;

    @Value("${mirror.schedule.tasks.password}")
    private String cheduleTasksPassword;

    @Value("${mirror.application.user-agent}")
    private String userAgent;

    @Scheduled(cron = "0 0/30 23 * * *")
    public void sendRequestSearchHistory() {
        logger.info("Запуск задания по сбору и отправке истории поиска в Python.");

        AuthResponseDto authResponseDto = keycloakAuthClient.authenticate(scheduleTasksUserName, cheduleTasksPassword);

        String uri = String.join("/", "", "request/history/send");

        webClient
                .post()
                .uri(uri)
                .header("Authorization", "Bearer " + authResponseDto.getAccessToken())
                .header(HttpHeaders.USER_AGENT, userAgent)
                .retrieve()
                .bodyToMono(Void.class)
                .log()
                .doOnError(err -> logger.error("Ошибка запуска удаленного сервиса - {}", err.getMessage()))
                .doOnSuccess(success -> logger.info("Выполнение задания по сбору и отправке истории поиска в Python завершено."))
                .subscribe();
    }
}
