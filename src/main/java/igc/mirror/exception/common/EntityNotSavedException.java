package igc.mirror.exception.common;

public class EntityNotSavedException extends EntityException{
    public EntityNotSavedException(String message, Long objectId, Class entityClass) {
        super(message, objectId, entityClass);
    }
}
