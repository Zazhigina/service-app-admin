package igc.mirror.service.controller;

import igc.mirror.service.dto.OfferTypeDto;
import igc.mirror.service.dto.ServiceOfferTypeDto;
import igc.mirror.service.filter.ServiceOfferTypeSearchCriteria;
import igc.mirror.service.service.ServiceOfferTypeService;
import igc.mirror.utils.qfilter.DataFilter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Ведение маппинга услуг и шаблонов КП")
public class ServiceOfferTypeController {
    @Autowired
    ServiceOfferTypeService serviceOfferTypeService;

    @Operation(summary = "Перечень записей таблицы маппинга услуг и типов КП")
    @PostMapping(path = "service-offer-type/filter")
    public Page<ServiceOfferTypeDto> findServiceOfferTypes(@RequestBody(required = false) DataFilter<ServiceOfferTypeSearchCriteria> filter, Pageable pageable) {
        return serviceOfferTypeService.findServiceOfferTypeByFilter(filter, pageable);
    }

    @Operation(summary = "Сохранение записей таблицы маппинга услуг и типов КП")
    @PutMapping(path = "service-offer-type")
    public void saveServicesOfferTypes(@RequestBody(required = true) List<ServiceOfferTypeDto> servicesOfferTypes) {
        serviceOfferTypeService.saveServicesOfferTypes(servicesOfferTypes);
    }

    @Operation(summary = "Тип КП по коду услуги")
    @GetMapping(path = "service-offer-type/{service}")
    public String getOfferTypeByServiceCode(@PathVariable String service) {
        return serviceOfferTypeService.getOfferTypeByServiceCode(service);
    }

    @Operation(summary = "Перечень типов КП")
    @GetMapping(path = "service-offer-type/offer-types")
    public List<OfferTypeDto> getOfferTypes() {
        return serviceOfferTypeService.getOfferTypes();
    }
}
