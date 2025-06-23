package igc.mirror.coefficientsetting.filter;

import igc.mirror.utils.qfilter.SearchCriteria;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
@Getter
@Setter
public class OfferCoefficientsSettingSearchCriteria extends SearchCriteria {
    /**
     * Идентификаторы
     */
    private List<Long> ids;

    /**
     * Метод определения НМЦ
     */
    private String calculationMethod;

    /**
     * Вид шаблона КП
     */
    private String offerType;

    /**
     * Тип расчета
     */
    private String calculationType;

    /**
     * Дата актуальности
     */
    private LocalDateTime relevanceDate;

    public OfferCoefficientsSettingSearchCriteria() {}
}