package igc.mirror.monitoring.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MonitoringStatistic {
    /**
     * Идентификатор журнала сервисов
     */
    private Long id;

    /**
     * Сервис
     */
    private String serviceName;

    /**
     * Адрес запроса
     */
    private String url;

    /**
     * Краткое содержание
     */
    private String summary;

    /**
     * Результат проверки
     */
    private String resultCheck;

    /**
     * Дата и время проверки
     */
    private LocalDateTime createDate;

    /**
     * Флаг удаления записи
     */
    private Boolean deleted;
}
