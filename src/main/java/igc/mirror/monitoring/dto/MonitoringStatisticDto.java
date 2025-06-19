package igc.mirror.monitoring.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class MonitoringStatisticDto {

    private String serviceName;
    private String url;
    private String summary;
    private String resultCheck;
    private LocalDateTime createDate;
}
