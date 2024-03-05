package igc.mirror.costrange.service;

import igc.mirror.auth.UserDetails;
import igc.mirror.costrange.model.CostRange;
import igc.mirror.costrange.repository.CostRangeRepository;
import igc.mirror.costrange.dto.CostRangeDto;
import igc.mirror.costrange.filter.CostRangeSearchCriteria;
import igc.mirror.exception.common.EntityNotFoundException;
import igc.mirror.exception.common.IllegalEntityStateException;
import igc.mirror.utils.qfilter.DataFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CostRangeService {

    @Autowired
    CostRangeRepository costRangeRepository;

    @Autowired
    private UserDetails userDetails;

    public Page<CostRangeDto> findCostRangeByFilters(DataFilter<CostRangeSearchCriteria> dataFilter, Pageable pageable) {

        DataFilter<CostRangeSearchCriteria> filter = Optional.ofNullable(dataFilter).orElseGet(DataFilter::new);
        CostRangeSearchCriteria criteria = Optional.ofNullable(filter.getSearchCriteria()).orElseGet(() -> new CostRangeSearchCriteria(null, null, null, null));

        Page<CostRange> costRanges = costRangeRepository.findCostRangeByFilters(criteria, pageable);

        return new PageImpl<>(costRanges.getContent().stream().map(CostRangeDto::new)
                    .collect(Collectors.toList()), pageable, costRanges.getTotalElements());
    }

    public CostRangeDto changeCostRange(CostRangeDto newCostRange) {
        //Проверка
        validateCostRangeBeforeChange(newCostRange);

        if (newCostRange.getId() != null) {
            CostRange сostRange = costRangeRepository.getCostRangeById(newCostRange.getId());

            if (сostRange == null)
                throw new EntityNotFoundException("Отсутствует запись id " + newCostRange.getId(), null, CostRangeDto.class);

            сostRange.setLowerBound(newCostRange.getLowerBound());
            сostRange.setUpperBound(newCostRange.getUpperBound());
            сostRange.setIntervalStep(newCostRange.getIntervalStep());
            сostRange.setRangeText(newCostRange.getRangeText());

            сostRange.setLastUpdateUser(userDetails.getUsername());

            costRangeRepository.updateCostRangeById(сostRange);

            return new CostRangeDto(сostRange);
        } else {
            CostRange сostRange = costRangeRepository.getCostRangeByKey(newCostRange);

            if (сostRange  != null)
                throw new IllegalEntityStateException("Запись уже существует!", null, CostRangeDto.class);

            сostRange = new CostRange(newCostRange);
            сostRange.setCreateUser(userDetails.getUsername());

            return costRangeRepository.insertServiceVersion(сostRange);
        }
    }

    private void validateCostRangeBeforeChange(CostRangeDto newCostRange) {

        if (newCostRange.getLowerBound() == null) {
            throw new EntityNotFoundException("Не указана Нижняя граница диапазона", null, CostRangeDto.class);
        }

        if (newCostRange.getUpperBound() == null ) {
            throw new EntityNotFoundException("Не указана Верхняя граница диапазона", null, CostRangeDto.class);
        }

        if (newCostRange.getIntervalStep() == null) {
            throw new EntityNotFoundException("Не указан Шаг интервала", null, CostRangeDto.class);
        }

        if (newCostRange.getRangeText() == null ||
                newCostRange.getRangeText().isEmpty()) {
            throw new EntityNotFoundException("Не указан Текст диапазона", null, CostRangeDto.class);
        }
    }

    public Long deleteCostRange(Long id) {

        CostRange сostRange = costRangeRepository.getCostRangeById(id);

        if (сostRange == null)
            throw new EntityNotFoundException("Отсутствует запись id " + id, null, CostRangeDto.class);

        return costRangeRepository.deleteServiceVersionById(id);
    }
}
