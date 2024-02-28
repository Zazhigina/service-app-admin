package igc.mirror.costrange.filter;

import igc.mirror.utils.qfilter.SearchCriteria;

import java.util.List;

public class CostRangeSearchCriteria extends SearchCriteria {

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

    public CostRangeSearchCriteria (String lowerBound,
                                    String upperBound,
                                    String intervalStep,
                                    String rangeText){
        this.lowerBound   = lowerBound;
        this.upperBound   = upperBound;
        this.intervalStep = intervalStep;
        this.rangeText    = rangeText;
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
}
