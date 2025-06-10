package igc.mirror.coefficientsetting.controller;

import igc.mirror.coefficientsetting.dto.OfferCoefficientSettingDto;
import igc.mirror.coefficientsetting.filter.OfferCoefficientsSettingSearchCriteria;
import igc.mirror.coefficientsetting.model.OfferCoefficientSetting;
import igc.mirror.coefficientsetting.service.CoefficientSettingService;
import igc.mirror.utils.qfilter.DataFilter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Настройки для коэффициентов в КП")
public class CoefficientSettingController {

    static final Logger logger = LoggerFactory.getLogger(CoefficientSettingController.class);

    private final CoefficientSettingService coefficientSettingService;

    public  CoefficientSettingController (CoefficientSettingService coefficientSettingService) {

        this.coefficientSettingService = coefficientSettingService;
    }

    @Operation(summary = "Изменение настройки для коэффициентов в КП")
    @PutMapping(path = "offer/coefficient/setting")
    public List<OfferCoefficientSetting> changeOfferCoefficientsSetting(@RequestBody List<OfferCoefficientSettingDto> coefficientsSetting) {

        return coefficientSettingService.changeOfferCoefficientsSetting(coefficientsSetting);
    }

    @Operation(summary = "Удаление настройки для коэффициентов в КП")
    @PostMapping(path = "offer/coefficient/setting/delete")
    public ResponseEntity<String> deleteOfferCoefficientsSetting(@RequestBody List<Long> ids) {

        return ResponseEntity.ok(coefficientSettingService.deleteOfferCoefficientsSetting(ids));
    }

    @Operation(summary = "Поиск настроек для коэффициентов в КП")
    @PostMapping(path = "offer/coefficient/setting/filter")
    public Page<OfferCoefficientSetting> findOfferCoefficientsSetting(@RequestBody(required = false) DataFilter<OfferCoefficientsSettingSearchCriteria> filter,
                                                                                                     Pageable                                           pageable) {

        return coefficientSettingService.findOfferCoefficientsSetting(filter, pageable);
    }
}