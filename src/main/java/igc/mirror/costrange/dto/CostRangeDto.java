package igc.mirror.costrange.dto;

import igc.mirror.costrange.model.CostRange;

import java.time.LocalDateTime;

public class CostRangeDto {

    /**
     * Идентификатор записи
     */
    private Long id;

    /**
     * Нижняя граница
     */
    private String lowerBound;

    /**
     * Верхняя граница
     */
    private String upperBound;

    /**
     * Шаг Интервала
     */
    private String intervalStep;

    /**
     * Текст диапазона
     */
    private String rangeText;

    /**
     * Дата и время создания
     */
    private LocalDateTime createDate;

    /**
     * Автор создания
     */
    private String createUser;

    /**
     * Дата и время изменения
     */
    private LocalDateTime lastUpdateDate;

    /**
     * Автор изменения
     */
    private String lastUpdateUser;

    public CostRangeDto() {
    }

    public CostRangeDto(CostRange costRange) {

        this.id           = costRange.getId();
        this.lowerBound   = costRange.getLowerBound();
        this.upperBound   = costRange.getUpperBound();
        this.intervalStep = costRange.getIntervalStep();
        this.rangeText    = costRange.getRangeText();

        this.createDate     = costRange.getCreateDate();
        this.createUser     = costRange.getCreateUser();
        this.lastUpdateDate = costRange.getLastUpdateDate();
        this.lastUpdateUser = costRange.getLastUpdateUser();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLowerBound() {
        return lowerBound;
    }

    public void setLowerBound(String lowerBound) {
        this.lowerBound = lowerBound;
    }

    public String getUpperBound() {
        return upperBound;
    }

    public void setUpperBound(String upperBound) {
        this.upperBound = upperBound;
    }

    public String getIntervalStep() {
        return intervalStep;
    }

    public void setIntervalStep(String intervalStep) {
        this.intervalStep = intervalStep;
    }

    public String getRangeText() {
        return rangeText;
    }

    public void setRangeText(String rangeText) {
        this.rangeText = rangeText;
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

    public LocalDateTime getLastUpdateDate() {
        return lastUpdateDate;
    }

    public void setLastUpdateDate(LocalDateTime lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }

    public String getLastUpdateUser() {
        return lastUpdateUser;
    }

    public void setLastUpdateUser(String lastUpdateUser) {
        this.lastUpdateUser = lastUpdateUser;
    }
}
