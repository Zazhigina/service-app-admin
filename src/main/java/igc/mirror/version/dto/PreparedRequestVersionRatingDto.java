package igc.mirror.version.dto;

import igc.mirror.question.ref.AnswerType;
import igc.mirror.version.ref.VersionProcessingStatus;

import java.time.LocalDateTime;
import java.util.Map;

public class PreparedRequestVersionRatingDto {
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
    private AnswerType answerType;

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

    public PreparedRequestVersionRatingDto() {
    }

    public PreparedRequestVersionRatingDto(RequestVersionRating requestVersionRating, Map<Long, AnswerType> answerTypesByIds) {
        this.requestVersionId = requestVersionRating.getRequestVersionId();
        this.requestId = requestVersionRating.getRequestId();
        this.answerType = answerTypesByIds.get(requestVersionRating.getAnswerId());
        this.comment = requestVersionRating.getComment();
        this.createDate = requestVersionRating.getCreateDate();
        this.createUser = requestVersionRating.getCreateUser();
        this.processingStatus = requestVersionRating.getProcessingStatus();
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

    public AnswerType getAnswerType() {
        return answerType;
    }

    public void setAnswerType(AnswerType answerType) {
        this.answerType = answerType;
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
