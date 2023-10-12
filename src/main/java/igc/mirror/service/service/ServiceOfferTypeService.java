package igc.mirror.service.service;

import igc.mirror.auth.UserDetails;
import igc.mirror.exception.common.IllegalEntityStateException;
import igc.mirror.nsi.model.ServiceProduct;
import igc.mirror.nsi.service.NSIService;
import igc.mirror.service.dto.OfferTypeDto;
import igc.mirror.service.dto.ServiceOfferTypeDto;
import igc.mirror.service.filter.ServiceOfferTypeSearchCriteria;
import igc.mirror.service.model.ServiceOfferType;
import igc.mirror.service.ref.OfferType;
import igc.mirror.service.repository.ServiceOfferTypeRepository;
import igc.mirror.utils.qfilter.DataFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ServiceOfferTypeService {
    @Autowired
    ServiceOfferTypeRepository serviceOfferTypeRepository;

    @Autowired
    NSIService nsiService;

    @Autowired
    private UserDetails userDetails;

    /**
     * Возвращает начальный запрос отбора маппинга услуга - вид КП
     *
     * @param filter   критерий поиска
     * @param pageable настройки пэджинации
     * @return список строк таблицы маппинга
     */
    public Page<ServiceOfferTypeDto> findServiceOfferTypeByFilter(DataFilter<ServiceOfferTypeSearchCriteria> filter, Pageable pageable) {
        List<ServiceOfferTypeDto> servicesOfferTypes = serviceOfferTypeRepository.findServiceOfferTypeByFilter(filter, pageable);
        if (servicesOfferTypes != null && !servicesOfferTypes.isEmpty()) {
            Map<String, ServiceProduct> serviceByCode = nsiService.getServicesProductsByCodes(servicesOfferTypes.stream().map(ServiceOfferTypeDto::getServiceCode).toList());
            servicesOfferTypes.stream().peek(r -> {
                ServiceProduct service = serviceByCode.get(r.getServiceCode());
                r.setServiceName((service != null ? service.getName() : null));
                if (r.getOfferType() != null)
                    r.setOfferTypeName(r.getOfferType().getName());
            }).collect(Collectors.toList());
        }

        long total = (servicesOfferTypes.size() >= pageable.getPageSize() ?
                serviceOfferTypeRepository.getServiceOfferTypesCount(filter) : servicesOfferTypes.size());

        return new PageImpl<>(servicesOfferTypes, pageable, total);
    }

    /**
     * Возвращает список видов КП
     *
     * @return список видов КП
     */
    public List<OfferTypeDto> getOfferTypes() {
        return Arrays.stream(OfferType.values()).map(r ->
                new OfferTypeDto(r)).collect(Collectors.toList());
    }

    /**
     * Возвращает вид КП по коду услуги
     *
     * @param service код услуги
     * @return вид КП
     */
    public String getOfferTypeByServiceCode(String service) {
        return serviceOfferTypeRepository.getOfferTypeByServiceCode(service);
    }

    /**
     * Сохранение записей таблицы маппинга услуг и типов КП
     *
     * @param servicesOfferTypesDto записи таблицы маппинга
     */
    @Transactional
    public void saveServicesOfferTypes(List<ServiceOfferTypeDto> servicesOfferTypesDto) {
        checkServicesOfferTypes(servicesOfferTypesDto);

        List<ServiceOfferType> modifyRecords = new ArrayList<>(servicesOfferTypesDto.stream()
                .map(r -> new ServiceOfferType(r, userDetails.getUsername())).collect(Collectors.toList()));
        Set<String> services = new HashSet<>(modifyRecords.stream().map(r -> r.getServiceCode()).collect(Collectors.toList()));

        List<ServiceOfferType> oldRecords = serviceOfferTypeRepository.findAll();
        Set<Long> recordsForDelete = new HashSet<>();
        for (ServiceOfferType record :
                oldRecords) {
            if (!services.contains(record.getServiceCode()))
                recordsForDelete.add(record.getId());
        }

        if (!recordsForDelete.isEmpty())
            serviceOfferTypeRepository.deleteServicesOfferTypes(recordsForDelete);

        if (!modifyRecords.isEmpty())
            serviceOfferTypeRepository.saveServicesOfferTypes(modifyRecords);

    }

    public void checkServicesOfferTypes(List<ServiceOfferTypeDto> servicesOfferTypes) {
        Set<String> services = new HashSet<>();
        Map<String, ServiceProduct> serviceByCode = nsiService.getServicesProductsByCodes(servicesOfferTypes.stream().map(ServiceOfferTypeDto::getServiceCode).toList());
        String dublicates = "";
        String notExistingServices = "";
        for (ServiceOfferTypeDto record :
                servicesOfferTypes) {
            if (services.contains(record.getServiceCode())) {
                if (dublicates.isEmpty())
                    dublicates = record.getServiceCode();
                else
                    dublicates = dublicates + ", " + record.getServiceCode();
            } else {
                services.add(record.getServiceCode());
            }

            if (serviceByCode.get(record.getServiceCode()) == null)
                if (notExistingServices.isEmpty())
                    notExistingServices = record.getServiceCode();
                else
                    notExistingServices = notExistingServices + ", " + record.getServiceCode();
        }

        if (!dublicates.isEmpty())
            throw new IllegalEntityStateException("Найдены дубли для услуг " + dublicates, null, ServiceOfferType.class);

        if (!notExistingServices.isEmpty())
            throw new IllegalEntityStateException("Найдены несуществующие услуги " + notExistingServices, null, ServiceOfferType.class);

    }
}
