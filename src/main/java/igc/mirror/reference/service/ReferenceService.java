package igc.mirror.reference.service;

import igc.mirror.nsi.model.ServiceProduct;
import igc.mirror.nsi.service.NSIService;
import igc.mirror.reference.dto.ServiceProductDto;
import igc.mirror.service.filter.ServiceProductSearchCriteria;
import igc.mirror.utils.qfilter.DataFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReferenceService {
    @Autowired
    NSIService nsiService;

    public Page<ServiceProductDto> findServiceProduct(DataFilter<ServiceProductSearchCriteria> filter, Pageable pageable) {
        Page<ServiceProduct> services = nsiService.getServicesProductsByFilter(filter, pageable);
        List<ServiceProductDto> servicesDto = services.map(r -> new ServiceProductDto(r)).toList();

        return new PageImpl<>(servicesDto, pageable, services.getTotalElements());
    }
}
