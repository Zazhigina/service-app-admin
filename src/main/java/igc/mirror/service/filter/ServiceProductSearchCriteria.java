package igc.mirror.service.filter;

import igc.mirror.utils.qfilter.SearchCriteria;

import java.time.LocalDateTime;
import java.util.List;

public class ServiceProductSearchCriteria extends SearchCriteria {
    private LocalDateTime fromDate;
    private LocalDateTime toDate;
    private String searchKey;
    private Boolean withHistData;
    private List<String> catalogs;

    public ServiceProductSearchCriteria(LocalDateTime fromDate, LocalDateTime toDate, String searchKey) {
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.searchKey = searchKey;
        this.withHistData = Boolean.FALSE;
    }

    public LocalDateTime getFromDate() {
        return fromDate;
    }

    public void setFromDate(LocalDateTime fromDate) {
        this.fromDate = fromDate;
    }

    public LocalDateTime getToDate() {
        return toDate;
    }

    public void setToDate(LocalDateTime toDate) {
        this.toDate = toDate;
    }

    public String getSearchKey() {
        return searchKey;
    }

    public void setSearchKey(String searchKey) {
        this.searchKey = searchKey;
    }

    public Boolean getWithHistData() {
        return withHistData;
    }

    public void setWithHistData(Boolean withHistData) {
        this.withHistData = withHistData;
    }

    public List<String> getCatalogs() {
        return catalogs;
    }

    public void setCatalogs(List<String> catalogs) {
        this.catalogs = catalogs;
    }
}