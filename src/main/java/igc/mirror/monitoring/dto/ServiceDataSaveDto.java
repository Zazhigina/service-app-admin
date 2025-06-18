package igc.mirror.monitoring.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ServiceDataSaveDto {

    @NotBlank(message = "Имя сервиса не может быть пустым")
    private String name;

    @NotBlank(message = "Описание не может быть пустым")
    String description;
}
