package igc.mirror.monitoring.mapper;

import igc.mirror.monitoring.dto.*;
import igc.mirror.monitoring.model.MonitoringData;
import igc.mirror.monitoring.model.MonitoringStatistic;
import org.mapstruct.*;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MonitoringDataMapper {

    MonitoringData saveDtoToEntity(MonitoringDataSaveDto dto);

    MonitoringDataDto entityToSaveDto(MonitoringData entity);

    MonitoringStatisticDto statisticToCheckDto(MonitoringStatistic monitoringStatistic);

}