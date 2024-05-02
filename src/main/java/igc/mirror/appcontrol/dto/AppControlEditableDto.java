package igc.mirror.appcontrol.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class AppControlEditableDto {
    @NotBlank(message = "Описание сервиса не должно быть пустым")
    private String description;
    @NotNull(message = "Значение режима работы сервиса не может быть пустым")
    private boolean enabled;

    public AppControlEditableDto(String description, Boolean enabled) {
        this.description = description;
        this.enabled = enabled;
    }

    public AppControlEditableDto() {
    }

    public String getDescription() { return description; }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
