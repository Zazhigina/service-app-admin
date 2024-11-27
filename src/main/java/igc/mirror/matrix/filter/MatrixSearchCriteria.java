package igc.mirror.matrix.filter;

import igc.mirror.utils.qfilter.SearchCriteria;

import java.time.LocalDateTime;

public class MatrixSearchCriteria extends SearchCriteria {
    private LocalDateTime fromDate;
    private LocalDateTime toDate;
    private String searchKey;

    public MatrixSearchCriteria(LocalDateTime fromDate, LocalDateTime toDate, String searchKey) {
        this.fromDate = (fromDate == null && toDate == null ? LocalDateTime.now() : fromDate);
        this.toDate = (toDate == null && fromDate == null ? LocalDateTime.now().plusDays(1L) : toDate);
        this.searchKey = searchKey;
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

    @Override
    public String toString() {
        return "ReferenceSimpleSearchCriteria{" +
                "fromDate=" + fromDate +
                ", toDate=" + toDate +
                ", searchKey='" + searchKey + '\'' +
                '}';
    }
}
