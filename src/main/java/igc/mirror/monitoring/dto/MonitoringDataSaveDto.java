package igc.mirror.monitoring.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.validator.constraints.URL;

@Data
public class MonitoringDataSaveDto {

    @NotBlank(message = "Имя сервиса не может быть пустым")
    private String name;

    @NotBlank(message = "URL не может быть пустым")
    @URL(message = "Ошибка: Некорректный формат URL")
    private String url;

    private String summary;
}
