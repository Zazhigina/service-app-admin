package igc.mirror.exception.handler;

import org.springframework.http.HttpStatus;

public class SuccessInfo {
    private int status;
    private String message;

    public SuccessInfo(String message) {
        this.status = HttpStatus.OK.value();
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
}
