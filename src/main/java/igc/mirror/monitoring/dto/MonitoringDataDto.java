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

    //private Long id;
    private String serviceName;
    private String url;
    private String summary;
    private String isActual;
}
