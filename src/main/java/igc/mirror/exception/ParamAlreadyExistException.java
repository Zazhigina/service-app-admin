package igc.mirror.exception;

public class ParamAlreadyExistException extends RuntimeException{
    public ParamAlreadyExistException() {
        super("Параметр уже существует");
    }
}
