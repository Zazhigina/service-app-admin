package igc.mirror.coefficientsetting.dto;

import igc.mirror.coefficientsetting.ref.CalculationMethod;
import igc.mirror.coefficientsetting.ref.CoefficientCalculationType;
import igc.mirror.service.ref.OfferType;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
@Getter
@Setter
public class OfferCoefficientSettingDto {
    /**
     * Id записи
     */
    private Long id;

    /**
     * Метод определения НМЦ
     */
    @NotNull
    private CalculationMethod calculationMethod;

    /**
     * Вид шаблона КП
     */
    @NotNull
    private OfferType offerType;

    /**
     * Тип расчета
     */
    @NotNull
    private CoefficientCalculationType calculationType;

    /**
     * Дата начала срока действия
     */
    private LocalDateTime startDate;

    /**
     * Дата окончания срока действия
     */
    private LocalDateTime endDate;
}