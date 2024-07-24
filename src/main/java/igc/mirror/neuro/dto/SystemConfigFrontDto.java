package igc.mirror.neuro.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Getter
@Setter
public class SystemConfigFrontDto {

    private LocalDateTime date;
    private Boolean isActive;
    private float configVersion;
    private float neuronetVersion;
    private String neuronetPath;
    private String reindexStatus;
    private List<IndexDto> defaultIndexes;

    public SystemConfigFrontDto(LocalDateTime date, Boolean isActive, float configVersion,
                           float neuronetVersion, String neuronetPath, String reindexStatus,
                                List<IndexDto> defaultIndexes) {
        this.date = date;
        this.isActive = isActive;
        this.configVersion = configVersion;
        this.neuronetVersion = neuronetVersion;
        this.neuronetPath = neuronetPath;
        this.reindexStatus = reindexStatus;
        this.defaultIndexes = defaultIndexes;
    }

    public SystemConfigDto convertToSystemConfigDto() {
        return new SystemConfigDto(
                this.date,
                this.isActive,
                this.configVersion,
                this.neuronetVersion,
                this.neuronetPath,
                this.reindexStatus,
                Map.ofEntries(
                        Map.entry("eis", this.defaultIndexes
                                .stream()
                                .filter(ind -> ind.getName().toLowerCase().contains("eis"))
                                .findFirst().orElse(new IndexDto(null,null))),
                        Map.entry("asuz", this.defaultIndexes
                                .stream()
                                .filter(ind -> ind.getName().toLowerCase().contains("asuz"))
                                .findFirst().orElse(new IndexDto(null,null))),
                        Map.entry("history", this.defaultIndexes
                                .stream()
                                .filter(ind -> ind.getName().toLowerCase().contains("history"))
                                .findFirst().orElse(new IndexDto(null,null)))
                )
        );
    }
}
