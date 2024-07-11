package igc.mirror.neuro.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NeuronetInfoDto {

    private String name;
    private float version;
    private String path;
    private Boolean isActive;
    private Boolean isLatest;

    public NeuronetInfoDto(String name, float version, String path, Boolean is_active, Boolean is_latest) {
        this.name = name;
        this.version = version;
        this.path = path;
        this.isActive = is_active;
        this.isLatest = is_latest;
    }
}
