package igc.mirror.monitoring.mapper;

import igc.mirror.monitoring.dto.*;
import igc.mirror.monitoring.model.MonitoringData;
import igc.mirror.monitoring.model.MonitoringStatistic;
import igc.mirror.monitoring.model.ServiceData;
import org.mapstruct.*;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MonitoringDataMapper {

    @Mapping(source = "name", target = "serviceData", qualifiedByName = "mapServiceDataByName")
    MonitoringData saveDtoToEntity(MonitoringDataSaveDto dto);

    @Mapping(source = "serviceData", target = "serviceData", qualifiedByName = "mapServiceDataDto")
    MonitoringDataDto entityToSaveDto(MonitoringData entity);

    @Mapping(source = "monitoringData.serviceData.name", target = "serviceName")
    @Mapping(source = "monitoringData.serviceData.description", target = "serviceDescription")
    @Mapping(source = "monitoringData.url", target = "url")
    @Mapping(source = "monitoringData.summary", target = "summary")
    MonitoringCheckDto statisticToCheckDto(MonitoringStatistic monitoringStatistic);

    @Named("mapServiceDataByName")
    default ServiceData mapServiceDataByName(String name) {
        // Проводим поиск по имени сервиса в базе данных
        if (name == null) {
            return null;
        }
        return new ServiceData(null, name, null);  // временная заглушка для маппинга
    }

    @Named("mapServiceDataDto")
    default ServiceDataDto mapServiceDataDto(ServiceData serviceData) {
        if (serviceData == null) return null;
        return new ServiceDataDto(serviceData.getName(), serviceData.getDescription());
    }

}