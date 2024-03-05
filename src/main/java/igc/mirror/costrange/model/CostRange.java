package igc.mirror.costrange.model;

import igc.mirror.costrange.dto.CostRangeDto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class CostRange {

    /**
     * Идентификатор записи
     */
    private Long id;

    /**
     * Нижняя граница
     */
    private BigDecimal lowerBound;

    /**
     * Верхняя граница
     */
    private BigDecimal upperBound;

    /**
     * Шаг Интервала
     */
    private BigDecimal intervalStep;

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

    public CostRange() {
    }

    public CostRange(CostRangeDto costRange) {

        this.id           = costRange.getId();
        this.lowerBound   = costRange.getLowerBound();
        this.upperBound   = costRange.getUpperBound();
        this.intervalStep = costRange.getIntervalStep();
        this.rangeText    = costRange.getRangeText();

        //this.createDate     = costRange.getCreateDate();
        this.createUser     = costRange.getCreateUser();
        // this.lastUpdateDate = costRange.getLastUpdateDate();
        this.lastUpdateUser = costRange.getLastUpdateUser();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getLowerBound() {
        return lowerBound;
    }

    public void setLowerBound(BigDecimal lowerBound) {
        this.lowerBound = lowerBound;
    }

    public BigDecimal getUpperBound() {
        return upperBound;
    }

    public void setUpperBound(BigDecimal upperBound) {
        this.upperBound = upperBound;
    }

    public BigDecimal getIntervalStep() {
        return intervalStep;
    }

    public void setIntervalStep(BigDecimal intervalStep) {
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
