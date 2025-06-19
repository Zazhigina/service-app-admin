package igc.mirror.monitoring.dto;

import lombok.Data;
import org.hibernate.validator.constraints.URL;

@Data
public class MonitoringDataUpdateDto {

    private String serviceName;

    @URL(message = "Ошибка: Некорректный формат URL")
    private String url;

    private String summary;

    private Boolean isActive;
}
