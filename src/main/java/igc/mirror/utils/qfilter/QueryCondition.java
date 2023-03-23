package igc.mirror.utils.qfilter;

public class QueryCondition {
    private String field;
    private ComparisonOperator operator;
    private String value;
    private String format;
    private QueryFilter filter;

    public QueryCondition() {
    }

    public QueryCondition(String field, ComparisonOperator operator, String value) {
        this.field = field;
        this.operator = operator;
        this.value = value;
    }

    public QueryCondition(String field, ComparisonOperator operator, String value, QueryFilter filter) {
        this.field = field;
        this.operator = operator;
        this.value = value;
        this.filter = filter;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public ComparisonOperator getOperator() {
        return this.operator;
    }

    public void setOperator(ComparisonOperator operator) {
        this.operator = operator;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public QueryFilter getFilter() {
        return filter;
    }

    public void setFilter(QueryFilter filter) {
        this.filter = filter;
    }

    @Override
    public String toString() {
        return "QueryCondition{" +
                field + " " +
                operator + " " +
                value + " " +
                format +
                ", filter=" + filter +
                '}';
    }
}
