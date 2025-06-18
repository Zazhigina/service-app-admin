package igc.mirror.monitoring.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class MonitoringDataDto {

    private ServiceDataDto serviceData;
    private String url;
    private String summary;
}
