package igc.mirror.exception.handler;

public class EntityExceptionInfo extends ExceptionInfo{
    private Long objectId;
    private Class entityClass;
    private String errorField;

    public EntityExceptionInfo(String cause, String advice, Long objectId, Class entityClass) {
        super(cause, advice);
        this.objectId = objectId;
        this.entityClass = entityClass;
    }

    public EntityExceptionInfo(String cause, String advice, Long objectId, Class entityClass, String errorField) {
        this(cause, advice, objectId, entityClass);
        this.errorField = errorField;
    }

    public Long getObjectId() {
        return objectId;
    }

    public Class getEntityClass() {
        return entityClass;
    }

    public String getErrorField() {
        return errorField;
    }

}
