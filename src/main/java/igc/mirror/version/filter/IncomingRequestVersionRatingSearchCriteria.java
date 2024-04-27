package igc.mirror.version.filter;

import igc.mirror.question.ref.AnswerType;
import igc.mirror.utils.qfilter.SearchCriteria;
import igc.mirror.version.ref.VersionProcessingStatus;

import java.time.LocalDateTime;
import java.util.List;

public class IncomingRequestVersionRatingSearchCriteria extends SearchCriteria {
    /**
     * Дата начала
     */
    private LocalDateTime fromDate;

    /**
     * Дата окончания
     */
    private LocalDateTime toDate;

    /**
     * Типы ответов
     */
    private List<AnswerType> answerTypes;

    /**
     * Статус обработки
     */
    private VersionProcessingStatus processingStatus;

    public IncomingRequestVersionRatingSearchCriteria() {
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

    public List<AnswerType> getAnswerTypes() {
        return answerTypes;
    }

    public void setAnswerTypes(List<AnswerType> answerTypes) {
        this.answerTypes = answerTypes;
    }

    public VersionProcessingStatus getProcessingStatus() {
        return processingStatus;
    }

    public void setProcessingStatus(VersionProcessingStatus processingStatus) {
        this.processingStatus = processingStatus;
    }
}
