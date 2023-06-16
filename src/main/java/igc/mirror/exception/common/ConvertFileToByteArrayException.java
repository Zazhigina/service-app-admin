package igc.mirror.exception.common;

public class ConvertFileToByteArrayException extends RuntimeException {
    public ConvertFileToByteArrayException(String message) {
        super(message);
    }

    public ConvertFileToByteArrayException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
