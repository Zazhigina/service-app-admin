package igc.mirror.utils.qfilter;

public class DataFilter<T extends SearchCriteria> {
    private T searchCriteria;
    private QueryFilter subFilter;

    public DataFilter() {
    }

    public DataFilter(T searchCriteria, QueryFilter subFilter) {
        this.searchCriteria = searchCriteria;
        this.subFilter = subFilter;
    }

    public T getSearchCriteria() {
        return searchCriteria;
    }

    public void setSearchCriteria(T searchCriteria) {
        this.searchCriteria = searchCriteria;
    }

    public QueryFilter getSubFilter() {
        return subFilter;
    }

    public void setSubFilter(QueryFilter subFilter) {
        this.subFilter = subFilter;
    }
}
