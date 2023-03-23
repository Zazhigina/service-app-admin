package igc.mirror.exception.common;

public class EntityNotFoundException extends EntityException {
    private static final String DEFAULT_MESSAGE = "Объект не найден";

    public EntityNotFoundException(String message, Long objectId, Class entityClass) {
        super(message, objectId, entityClass);
    }

    public EntityNotFoundException(Long objectId, Class entityClass) {
        super(objectId, entityClass);
    }

    @Override
    public String getDefaultMessage() {
        return DEFAULT_MESSAGE;
    }
}
