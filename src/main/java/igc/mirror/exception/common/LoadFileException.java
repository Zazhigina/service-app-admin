package igc.mirror.exception.common;

public class LoadFileException extends RuntimeException {
    public LoadFileException(String message) {
        super(message);
    }

    public LoadFileException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
