package igc.mirror.task.dto;

import java.time.LocalDate;

public class EISContractsUploadRequestDto {
    /**
     * Дата начало периода загрузки
     */
    private LocalDate downloadDateFrom;
    /**
     * Дата окончания периода загрузки
     */
    private LocalDate downloadDateTo;

    public EISContractsUploadRequestDto(LocalDate downloadDateFrom, LocalDate downloadDateTo) {
        this.downloadDateFrom = downloadDateFrom;
        this.downloadDateTo = downloadDateTo;
    }

    public LocalDate getDownloadDateFrom() {
        return downloadDateFrom;
    }
    public LocalDate getDownloadDateTo() {
        return downloadDateTo;
    }
}
