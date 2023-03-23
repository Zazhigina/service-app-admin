package igc.mirror.exception.handler;

import org.springframework.http.HttpStatus;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

public class ExceptionInfo {
    private Date timestamp;
    private int status;
    private String error;
    private String path;
    private String cause;
    private String advice;

    private List<DetailExceptionInfo> details;

    public ExceptionInfo(String cause, String advice) {
        this.cause = cause;
        this.advice = advice;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public int getStatus() {
        return status;
    }

    public String getError() {
        return error;
    }

    public String getPath() {
        return path;
    }

    public String getCause() {
        return cause;
    }

    public String getAdvice() {
        return advice;
    }

    public void setPublicErrorInfo(HttpServletRequest request, HttpStatus status){
        this.timestamp = new Date();
        this.error = this.cause;
        this.status = status.value();
        this.path = request.getRequestURI();
    }

    public void setPublicErrorInfo(HttpServletRequest request, HttpStatus status, Throwable throwable){
        this.timestamp = new Date();
        this.error = throwable.getCause().getMessage();
        this.status = status.value();
        this.path = request.getRequestURI();
    }

    public List<DetailExceptionInfo> getDetails() {
        return details;
    }

    public void setDetails(List<DetailExceptionInfo> details) {
        this.details = details;
    }
}
