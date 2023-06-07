package igc.mirror.exception.common;

public class EntityNotSavedException extends EntityException{
    public EntityNotSavedException(String message, Long objectId, Class entityClass) {
        super(message, objectId, entityClass);
    }

    public EntityNotSavedException(Long objectId, Class entityClass) {
        super(objectId, entityClass);
    }
}
