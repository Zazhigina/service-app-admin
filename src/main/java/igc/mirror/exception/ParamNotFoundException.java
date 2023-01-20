package igc.mirror.exception;

public class ParamNotFoundException extends RuntimeException {
    public ParamNotFoundException() {
        super("Параметр не существует");
    }

    public ParamNotFoundException(String message) {
        super(message);
    }
}
