package igc.mirror.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.reactive.function.client.WebClient;

public class RequestSearchHistoryTask {
    static final Logger logger = LoggerFactory.getLogger(RequestSearchHistoryTask.class);

    @Autowired
    @Qualifier("ma")
    private WebClient webClient;

    @Scheduled(cron = "0 0/30 23 * * *")
    public void sendRequestSearchHistory() {
        logger.info("Запуск задания по сбору и отправке истории поиска в Python.");

        String uri = String.join("/", "", "request/history/send");

        webClient
                .post()
                .uri(uri)
                .retrieve()
                .bodyToMono(Void.class)
                .log()
                .doOnError(err -> logger.error("Ошибка запуска удаленного сервиса - {}", err.getMessage()))
                .doOnSuccess(success -> logger.info("Выполнение задания по сбору и отправке истории поиска в Python завершено."))
                .subscribe();
    }
}
