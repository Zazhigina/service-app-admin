package igc.mirror.exception.handler;

import com.fasterxml.jackson.annotation.JsonInclude;

public class DetailExceptionInfo {
    private String message;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String field;

    public DetailExceptionInfo(String message, String field) {
        this.field = field;
        this.message = message;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
