package igc.mirror.monitoring.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.validator.constraints.URL;

@Data
public class MonitoringDataUpdateDto {

    private String name;

    @URL(message = "Ошибка: Некорректный формат URL")
    private String url;

    private String summary;

    private Boolean isActive;
}
