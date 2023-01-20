package igc.mirror.utils.qfilter;

import com.fasterxml.jackson.annotation.JsonValue;

public enum ComparisonOperator {
    LIKE("like"),

    NOT_LIKE("not_like"),

    LIKE_IGNORE_CASE("ilike"),

    NOT_LIKE_IGNORE_CASE("not_ilike"),

    EQ("eq"),

    NOT_EQ("not_eq"),

    GT("gt"),

    GTE("gte"),

    LT("lt"),

    LTE("lte"),

    NULL("null"),

    NOT_NULL("not_null");

    private String operator;

    ComparisonOperator(String operator) {
        this.operator = operator;
    }

    @JsonValue
    public String getOperator() {
        return this.operator;
    }

}

