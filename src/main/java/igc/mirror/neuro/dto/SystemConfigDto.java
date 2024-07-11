package igc.mirror.neuro.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@Getter
@Setter
public class SystemConfigDto {

    private LocalDateTime date;
    private Boolean isActive;
    private float configVersion;
    private float neuronetVersion;
    private String neuronetPath;
    private String reindexStatus;
    private Map<String, Object> defaultIndexes;

    public SystemConfigDto(LocalDateTime date, Boolean is_active, float config_version,
                           float neuronet_version, String neuronet_path, String reindex_status,
                           Map<String, Object> default_indexes) {
        this.date = date;
        this.isActive = is_active;
        this.configVersion = config_version;
        this.neuronetVersion = neuronet_version;
        this.neuronetPath = neuronet_path;
        this.reindexStatus = reindex_status;
        this.defaultIndexes = default_indexes;
    }

    public Map<String, Object> convertToPySchema() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        return Map.ofEntries(
            Map.entry("date", this.date.format(formatter)),
            Map.entry("is_active", this.isActive),
            Map.entry("config_version", this.configVersion),
            Map.entry("neuronet_version", this.neuronetVersion),
            Map.entry("neuronet_path", this.neuronetPath),
            Map.entry("reindex_status", this.reindexStatus),
            Map.entry("default_indexes", this.defaultIndexes)
        );
    }

}
