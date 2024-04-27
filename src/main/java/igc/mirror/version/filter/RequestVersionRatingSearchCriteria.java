package igc.mirror.version.filter;

import igc.mirror.question.ref.AnswerType;
import igc.mirror.utils.qfilter.SearchCriteria;
import igc.mirror.version.ref.VersionProcessingStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public class RequestVersionRatingSearchCriteria extends SearchCriteria {
    /**
     * Дата начала
     */
    private LocalDateTime fromDate;

    /**
     * Дата окончания
     */
    private LocalDateTime toDate;

    /**
     * Идентификаторы ответов
     */
    private List<Long> answerIds;

    /**
     * Статус обработки
     */
    private VersionProcessingStatus processingStatus;

    public RequestVersionRatingSearchCriteria() {
    }

    public RequestVersionRatingSearchCriteria(IncomingRequestVersionRatingSearchCriteria incomingCriteria, Map<Long, AnswerType> answerTypesByIds) {
        this.fromDate = incomingCriteria.getFromDate();
        this.toDate = incomingCriteria.getToDate();
        this.answerIds = answerTypesByIds.keySet().stream().toList();
        this.processingStatus = incomingCriteria.getProcessingStatus();
    }

    public LocalDateTime getFromDate() {
        return fromDate;
    }

    public void setFromDate(LocalDateTime fromDate) {
        this.fromDate = fromDate;
    }

    public LocalDateTime getToDate() {
        return toDate;
    }

    public void setToDate(LocalDateTime toDate) {
        this.toDate = toDate;
    }

    public List<Long> getAnswerIds() {
        return answerIds;
    }

    public void setAnswerIds(List<Long> answerIds) {
        this.answerIds = answerIds;
    }

    public VersionProcessingStatus getProcessingStatus() {
        return processingStatus;
    }

    public void setProcessingStatus(VersionProcessingStatus processingStatus) {
        this.processingStatus = processingStatus;
    }
}
