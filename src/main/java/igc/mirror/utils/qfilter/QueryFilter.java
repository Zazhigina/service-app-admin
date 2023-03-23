package igc.mirror.utils.qfilter;

import java.util.List;

public class QueryFilter {
    private LogicalOperator logic;
    private List<QueryCondition> cond;

    private String selectField;

    public QueryFilter() {
    }

    public QueryFilter(List<QueryCondition> conditions) {
        this.cond = conditions;
    }

    public LogicalOperator getLogic() {
        return this.logic;
    }

    public void setLogic(LogicalOperator logic) {
        this.logic = logic;
    }

    public List<QueryCondition> getCond() {
        return cond;
    }

    public void setCond(List<QueryCondition> cond) {
        this.cond = cond;
    }

    public String getSelectField() {
        return selectField;
    }

    public void setSelectField(String selectField) {
        this.selectField = selectField;
    }
}
