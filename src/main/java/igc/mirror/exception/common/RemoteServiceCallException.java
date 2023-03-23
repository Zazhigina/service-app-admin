package igc.mirror.exception.common;

import igc.mirror.exception.handler.ExceptionInfo;
import org.springframework.http.HttpStatus;

public class RemoteServiceCallException extends Exception{
    private HttpStatus remoteStatus;

    private String remoteUrl;

    private String originalMessage;

    public RemoteServiceCallException(String message, HttpStatus remoteStatus, String remoteUrl, String originalMessage) {
        super(message);
        this.remoteStatus = remoteStatus;
        this.remoteUrl = remoteUrl;
        this.originalMessage = originalMessage;
    }

    public RemoteServiceCallException(String message, Throwable throwable) {
        super(message, throwable);
    }

    public HttpStatus getRemoteStatus() {
        return remoteStatus;
    }

    public String getRemoteUrl() {
        return remoteUrl;
    }

    public String getOriginalMessage() {
        return originalMessage;
    }

    public ExceptionInfo getExceptionInfo() {
        return new ExceptionInfo(getMessage(), null);
    }
}
