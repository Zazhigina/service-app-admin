package igc.mirror.exception.common;

public class EntityDuplicatedException extends EntityException{
    public EntityDuplicatedException(String message, Long objectId, Class entityClass) {
        super(message, objectId, entityClass);
    }
}
