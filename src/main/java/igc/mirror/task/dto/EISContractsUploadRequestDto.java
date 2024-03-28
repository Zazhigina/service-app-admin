package igc.mirror.task.dto;

import java.time.LocalDateTime;

public class EISContractsUploadRequestDto {
    /**
     * Дата начало периода загрузки
     */
    private LocalDateTime downloadDateFrom;
    /**
     * Дата окончания периода загрузки
     */
    private LocalDateTime downloadDateTo;

    public EISContractsUploadRequestDto(LocalDateTime downloadDateFrom, LocalDateTime downloadDateTo) {
        this.downloadDateFrom = downloadDateFrom;
        this.downloadDateTo = downloadDateTo;
    }

    public LocalDateTime getDownloadDateFrom() {
        return downloadDateFrom;
    }
    public LocalDateTime getDownloadDateTo() {
        return downloadDateTo;
    }
}
