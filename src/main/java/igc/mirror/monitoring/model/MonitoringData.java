package igc.mirror.monitoring.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MonitoringData {
    /**
     * Идентификатор журнала сервисов
     */
    private Long id;

    /**
     * Сервис
     */
    private ServiceData serviceData;

    /**
     * Адрес запроса
     */
    private String url;

    /**
     * Краткое содержание
     */
    private String summary;

    /**
     * Флаг актуальности записи
     */
    private Boolean isActive;



}
