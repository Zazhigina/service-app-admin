package igc.mirror.task;

import igc.mirror.auth.client.KeycloakAuthClient;
import igc.mirror.auth.dto.AuthResponseDto;
import igc.mirror.config.LoggingConstants;
import igc.mirror.exception.common.RemoteServiceCallException;
import igc.mirror.task.dto.EISContractsUploadRequestDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.UUID;

import static igc.mirror.config.LoggingConstants.USER_AGENT_KEY;
import static igc.mirror.config.LoggingConstants.X_REQUEST_ID_KEY;

public class EisContractsTask {
    static final Logger logger = LoggerFactory.getLogger(EisContractsTask.class);

    @Autowired
    @Qualifier("integration")
    private WebClient webClient;

    @Autowired
    private KeycloakAuthClient keycloakAuthClient;

    @Value("${mirror.schedule.tasks.username}")
    private String scheduleTasksUserName;

    @Value("${mirror.schedule.tasks.password}")
    private String scheduleTasksPassword;

    @Value("${mirror.application.user-agent}")
    private String userAgent;

    @Scheduled(cron = "${mirror.schedule.tasks.cron.replicate-procedure-records}")
    public void replicateProcedureRecords() {
        MDC.put(USER_AGENT_KEY, userAgent);
        MDC.put(X_REQUEST_ID_KEY, UUID.randomUUID().toString());

        logger.info("Подготовка к запуску задания по приему закупочных процедур для репликации в целевую систему");

        AuthResponseDto authResponseDto = keycloakAuthClient.authenticate(scheduleTasksUserName, scheduleTasksPassword);

        EISContractsUploadRequestDto uploadPeriod = new EISContractsUploadRequestDto(YearMonth.now().atDay(1).minusMonths(1), LocalDate.now());

        logger.info("Подготовка к запуску задания по приему закупочных процедур для репликации в целевую систему завершена. Период загрузки {} - {}",
                uploadPeriod.getDownloadDateFrom(), uploadPeriod.getDownloadDateTo());

        String uri = String.join("/", "", "effect/exchange/eis/contracts");

        webClient
                .post()
                .uri(uri)
                .header("Authorization", "Bearer " + authResponseDto.getAccessToken())
                .header(HttpHeaders.USER_AGENT, userAgent)
                .header(LoggingConstants.X_REQUEST_ID_HEADER, MDC.get(X_REQUEST_ID_KEY))
                .body(Mono.just(uploadPeriod), EISContractsUploadRequestDto.class)
                .retrieve()
                .bodyToMono(Long.class)
                .log()
                .onErrorMap(WebClientRequestException.class, throwable -> new RemoteServiceCallException("Неизвестный url", HttpStatus.INTERNAL_SERVER_ERROR, uri, throwable.getMessage()))
                .doOnError(err -> logger.error("Ошибка запуска удаленного сервиса - {}", err.getMessage()))
                .subscribe(exchangeId -> logger.info("Задание по приему закупочных процедур для репликации в целевую систему запущено. Идентификатор записи журнала - {}", exchangeId));

        MDC.remove(USER_AGENT_KEY);
        MDC.remove(X_REQUEST_ID_KEY);
    }
}
