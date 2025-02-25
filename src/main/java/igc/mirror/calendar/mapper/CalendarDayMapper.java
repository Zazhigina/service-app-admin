package igc.mirror.calendar.mapper;

import igc.mirror.calendar.dto.CalendarDayDto;
import igc.mirror.calendar.model.CalendarDay;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CalendarDayMapper {

    CalendarDayMapper INSTANCE = Mappers.getMapper(CalendarDayMapper.class);

    CalendarDayDto toDto(CalendarDay model);

    @InheritInverseConfiguration(name = "toDto")
    CalendarDay fromDto(CalendarDayDto dto);

}
