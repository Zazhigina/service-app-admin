package igc.mirror.appcontrol.dto;

import jakarta.validation.constraints.NotNull;

public class AppControlEnabledAllDto {
    @NotNull(message = "Значение режима работы сервисов не может быть пустым")
    private boolean enabled;

    public boolean getEnabled() {
        return enabled;
    }
}
