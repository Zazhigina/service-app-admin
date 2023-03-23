package igc.mirror.utils.qfilter;

import com.fasterxml.jackson.annotation.JsonValue;

public enum LogicalOperator {
    AND("and"), OR("or"), NOT("not");
    private String operator;

    LogicalOperator(String operator) {
        this.operator = operator;
    }

    @JsonValue
    public String getOperator() {
        return this.operator;
    }
}
