package igc.mirror.monitoring.mapper;

import igc.mirror.monitoring.dto.ServiceDataDto;
import igc.mirror.monitoring.model.ServiceData;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)

public interface ServiceDataMapper {
    ServiceDataDto entityToSaveDto(ServiceData entity);

}
