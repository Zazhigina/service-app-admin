package igc.mirror.reference.controller;

import igc.mirror.reference.service.ReferenceService;
import igc.mirror.reference.dto.ServiceProductDto;
import igc.mirror.service.filter.ServiceProductSearchCriteria;
import igc.mirror.utils.qfilter.DataFilter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("service-product")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Справочники")
public class ReferenceController {

    @Autowired
    ReferenceService referenceService;

    @Operation(summary = "Справочник услуг")
    @PostMapping(path = "/filter")
    public Page<ServiceProductDto> findServiceProduct(@RequestBody(required = false) DataFilter<ServiceProductSearchCriteria> filter,
                                                      Pageable pageable) {

        return referenceService.findServiceProduct(filter, pageable);
    }
}
