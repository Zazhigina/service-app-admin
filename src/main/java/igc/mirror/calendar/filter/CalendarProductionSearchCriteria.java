package igc.mirror.calendar.filter;

import igc.mirror.utils.qfilter.SearchCriteria;

public class CalendarProductionSearchCriteria extends SearchCriteria {
    private Integer year;

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public CalendarProductionSearchCriteria() {
        super();
    }
}