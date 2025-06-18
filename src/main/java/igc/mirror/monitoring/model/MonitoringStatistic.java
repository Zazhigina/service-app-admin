package igc.mirror.monitoring.model;

import igc.mirror.monitoring.dto.MonitoringDataDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MonitoringStatistic {
    private Long id;
    private MonitoringDataDto monitoringData;
    private String resultCheck;
    private LocalDateTime createDate;
}
