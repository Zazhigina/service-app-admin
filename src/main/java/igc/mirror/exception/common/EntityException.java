package igc.mirror.exception.common;

import igc.mirror.exception.handler.EntityExceptionInfo;
import igc.mirror.exception.handler.ExceptionInfo;

import java.util.Optional;

public abstract class EntityException extends RuntimeException {
    private Long objectId;
    private final Class entityClass;

    public EntityException(String message, Long objectId, Class entityClass) {
        super(message);
        this.objectId = objectId;
        this.entityClass = entityClass;
    }

    public EntityException(Long objectId, Class entityClass) {
        super();
        this.objectId = objectId;
        this.entityClass = entityClass;
    }

    public ExceptionInfo getExceptionInfo() {
        return new EntityExceptionInfo(getMessage(), null, this.objectId, this.entityClass);
    }

    public Long getObjectId() {
        return objectId;
    }

    public Class getEntityClass() {
        return entityClass;
    }

    public String getDefaultMessage() {
        return null;
    }

    @Override
    public String getMessage() {
        return Optional.ofNullable(super.getMessage()).orElse(getDefaultMessage());
    }
}
