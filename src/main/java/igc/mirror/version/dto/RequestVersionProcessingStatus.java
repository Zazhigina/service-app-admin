package igc.mirror.version.dto;

import igc.mirror.version.ref.VersionProcessingStatus;

public class RequestVersionProcessingStatus {
    /**
     * Идентификатор версии поискового запроса
     */
    private Long requestVersionId;

    /**
     * Статус обработки
     */
    private VersionProcessingStatus processingStatus;

    public RequestVersionProcessingStatus() {
    }

    public Long getRequestVersionId() {
        return requestVersionId;
    }

    public void setRequestVersionId(Long requestVersionId) {
        this.requestVersionId = requestVersionId;
    }

    public VersionProcessingStatus getProcessingStatus() {
        return processingStatus;
    }

    public void setProcessingStatus(VersionProcessingStatus processingStatus) {
        this.processingStatus = processingStatus;
    }
}
