package igc.mirror.version.dto;

import igc.mirror.version.ref.VersionProcessingStatus;

import java.time.LocalDateTime;

public class RequestVersionRating {
    /**
     * Идентификатор версии поискового запроса
     */
    private Long requestVersionId;

    /**
     * Идентификатор поискового запроса
     */
    private Long requestId;

    /**
     * Оценка версии поиска
     */
    private Long answerId;

    /**
     * Комментарий к оценке
     */
    private String comment;

    /**
     * Дата оценки
     */
    private LocalDateTime createDate;

    /**
     * Пользователь
     */
    private String createUser;

    /**
     * Статус обработки
     */
    private VersionProcessingStatus processingStatus;

    public RequestVersionRating() {
    }

    public Long getRequestVersionId() {
        return requestVersionId;
    }

    public void setRequestVersionId(Long requestVersionId) {
        this.requestVersionId = requestVersionId;
    }

    public Long getRequestId() {
        return requestId;
    }

    public void setRequestId(Long requestId) {
        this.requestId = requestId;
    }

    public Long getAnswerId() {
        return answerId;
    }

    public void setAnswerId(Long answerId) {
        this.answerId = answerId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public LocalDateTime getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDateTime createDate) {
        this.createDate = createDate;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public VersionProcessingStatus getProcessingStatus() {
        return processingStatus;
    }

    public void setProcessingStatus(VersionProcessingStatus processingStatus) {
        this.processingStatus = processingStatus;
    }
}
