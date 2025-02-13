package igc.mirror.calendar.filter;

import igc.mirror.utils.qfilter.SearchCriteria;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class CalendarDaySearchCriteria extends SearchCriteria {
    private Integer year;
    private Integer month;
}