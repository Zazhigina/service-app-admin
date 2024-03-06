package igc.mirror.costrange.controller;

import igc.mirror.costrange.service.CostRangeService;
import igc.mirror.costrange.dto.CostRangeDto;
import igc.mirror.costrange.filter.CostRangeSearchCriteria;
import igc.mirror.utils.qfilter.DataFilter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("cost-range")
@Tag(name = "Диапазоны стоимостных показателей")
public class CostRangeController {

    @Autowired
    CostRangeService costRangeService;

    @PostMapping("/filter")
    @Operation(summary = "Диапазоны стоимостных показателей")
    @PreAuthorize("hasAuthority('CONFIG_VALUE.READ')")
    public Page<CostRangeDto> findCostRangeByFilters(@RequestBody(required = false) DataFilter<CostRangeSearchCriteria> filter,
                                                     Pageable pageable) {
        return costRangeService.findCostRangeByFilters(filter, pageable);
    }

    @GetMapping("")
    @Operation(summary = "Диапазоны стоимостных показателей")
    public List<CostRangeDto> getCostRanges() {
        return costRangeService.getCostRanges();
    }

    @PutMapping("")
    @Operation(summary = "Сохранение/изменение диапазона стоимостных показателей")
    @PreAuthorize("hasAuthority('CONFIG_VALUE.CHANGE')")
    public CostRangeDto changeCostRange(@RequestBody CostRangeDto costRange) {

        return costRangeService.changeCostRange(costRange);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удаление диапазона стоимостных показателей")
    @PreAuthorize("hasAuthority('CONFIG_VALUE.CHANGE')")
    public Long deleteCostRange(@PathVariable Long id) {

        return costRangeService.deleteCostRange(id);
    }
}
