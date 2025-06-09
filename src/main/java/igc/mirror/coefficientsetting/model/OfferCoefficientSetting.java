package igc.mirror.coefficientsetting.model;

import igc.mirror.coefficientsetting.dto.OfferCoefficientSettingDto;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class OfferCoefficientSetting {
    /**
     * Id записи
     */
    private Long id;

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
     * Дата начала срока действия
     */
    private LocalDateTime startDate;

    /**
     * Дата окончания срока действия
     */
    private LocalDateTime endDate;

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

    public OfferCoefficientSetting(Long          id,
                                   String        calculationMethod,
                                   String        offerType,
                                   String        calculationType,
                                   LocalDateTime startDate,
                                   LocalDateTime endDate,
                                   LocalDateTime createDate,
                                   String        createUser,
                                   LocalDateTime lastUpdateDate,
                                   String        lastUpdateUser) {

        this.id                = id;
        this.calculationMethod = calculationMethod;
        this.offerType         = offerType;
        this.calculationType   = calculationType;
        this.startDate         = startDate;
        this.endDate           = endDate;

        this.createDate     = createDate;
        this.createUser     = createUser;
        this.lastUpdateDate = lastUpdateDate;
        this.lastUpdateUser = lastUpdateUser;
    }

    public OfferCoefficientSetting(OfferCoefficientSettingDto offerCoefficientSetting) {
        this(offerCoefficientSetting.getId(),
                offerCoefficientSetting.getCalculationMethod().name(),
                offerCoefficientSetting.getOfferType().name(),
                offerCoefficientSetting.getCalculationType().name(),
                offerCoefficientSetting.getStartDate(),
                offerCoefficientSetting.getEndDate(),
                null,
                null,
                null,
                null
        );
    }
}