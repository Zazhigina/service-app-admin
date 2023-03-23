package igc.mirror.exception.common;

import org.springframework.validation.Errors;

public class IllegalEntityStateException extends EntityException{
    private String errorFiled;

    private Errors errors;

    public IllegalEntityStateException(String message, Long objectId, Class entityClass) {
        super(message, objectId, entityClass);
    }

    public IllegalEntityStateException(String message, Long objectId, Class entityClass, String errorFiled) {
        super(message, objectId, entityClass);
        this.errorFiled = errorFiled;
    }

    public IllegalEntityStateException(String message, Long objectId, Class entityClass, Errors errors) {
        super(message, objectId, entityClass);
        this.errors = errors;
    }

    public Errors getErrors() {
        return errors;
    }

}
