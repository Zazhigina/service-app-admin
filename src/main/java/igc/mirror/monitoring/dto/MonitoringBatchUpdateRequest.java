package igc.mirror.monitoring.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class MonitoringBatchUpdateRequest {
    @JsonProperty(value = "isActive")
    private boolean isActive;
}

