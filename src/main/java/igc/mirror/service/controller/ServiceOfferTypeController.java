package igc.mirror.service.controller;

import igc.mirror.service.dto.*;
import igc.mirror.service.exchange.ReferenceSavingResult;
import igc.mirror.service.filter.ServiceOfferTypeSearchCriteria;
import igc.mirror.service.filter.ServiceProductSearchCriteria;
import igc.mirror.service.filter.ServiceVersionSearchCriteria;
import igc.mirror.service.service.ServiceOfferTypeService;
import igc.mirror.utils.qfilter.DataFilter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
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
    public ServiceOfferTypeForEPDto getOfferTypeByServiceCode(@PathVariable String service) {
        return serviceOfferTypeService.getOfferTypeByServiceCode(service);
    }

    @Operation(summary = "Перечень типов КП")
    @GetMapping(path = "service-offer-type/offer-types")
    public List<OfferTypeDto> getOfferTypes() {
        return serviceOfferTypeService.getOfferTypes();
    }

    @Operation(summary = "Справочник услуг")
    @PostMapping(path = "service-offer-type/service-product/filter")
    public Page<ServiceProductDto> findServiceProduct(@RequestBody(required = false) DataFilter<ServiceProductSearchCriteria> filter,
                                                      @RequestParam MultiValueMap<String, String> params, Pageable pageable) {

        return serviceOfferTypeService.findServiceProduct(filter, params, pageable);
    }

    @PostMapping("service-version/filter")
    @Operation(summary = "Mэппинг услуг справочника КТ-777")
    public Page<ServiceVersionDTO> findServiceVersionByFilters(@RequestBody(required = false) DataFilter<ServiceVersionSearchCriteria> filter,
                                                               Pageable pageable){
        return serviceOfferTypeService.findServiceVersionByFilters(filter, pageable);
    }

    @PutMapping("/service-version")
    @Operation(summary = "Схранение/изменение мэппинга услуг справочника КТ-777")
    public ResponseEntity<Resource> updateServiceVersion(@RequestBody ServiceVersionDTO serviceVersion){ //ServiceVersionDTO

        ResponseEntity<Resource> responseEntity = serviceOfferTypeService.changeServiceVersion(serviceVersion);

        HttpHeaders httpHeaders = new HttpHeaders();
        if (responseEntity.getHeaders().getContentDisposition() != null)
            httpHeaders.setContentDisposition(responseEntity.getHeaders().getContentDisposition());

        return ResponseEntity
                .status(responseEntity.getStatusCode())
                .contentType(responseEntity.getHeaders().getContentType())
                .headers(httpHeaders)
                .body(responseEntity.getBody());
    }

    @DeleteMapping("/service-version/{id}")
    @Operation(summary = "Удаление мэппинга услуг справочника КТ-777")
    public Long deleteServiceVersion(@PathVariable Long id){

        return serviceOfferTypeService.deleteServiceVersion(id);
    }

    @PostMapping("/service-version")
    @Operation(summary = "Загрузка мэппинга услуг справочника КТ-777")
    public ReferenceSavingResult updateServiceVersion(@RequestBody List<ServiceVersionDTO> listServiceVersion){
        return serviceOfferTypeService.uploadServiceVersion(listServiceVersion);
    }
}
