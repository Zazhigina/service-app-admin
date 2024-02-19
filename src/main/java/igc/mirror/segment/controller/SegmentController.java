package igc.mirror.segment.controller;

import igc.mirror.segment.ref.SegmentRecordType;
import igc.mirror.segment.service.SegmentService;
import igc.mirror.segment.view.SegmentDto;
import igc.mirror.segment.view.ServiceSegmentSubsegmentDto;
import igc.mirror.service.filter.SegmentSearchCriteria;
import igc.mirror.utils.qfilter.DataFilter;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class SegmentController {

    private final SegmentService segmentService;

    public SegmentController(SegmentService segmentService) {
        this.segmentService = segmentService;
    }

    @Operation(summary = "Справочник сегментов/подсегментов")
    @PostMapping(path = "/segment/filter")
    public Page<SegmentDto> getSegmentsByType(@RequestBody(required = false) DataFilter<SegmentSearchCriteria> filter,
                                              Pageable pageable){
        return segmentService.getSegmentsByFilter(filter,pageable);
    }

    @Operation(summary = "Актуальные сочетания услуга-сегмент-подсегмент")
    @GetMapping(path = "/service/{serviceCode}/{segmentRecordType}")
    public List<ServiceSegmentSubsegmentDto> getServiceSegmentSubsegmentsByServiceCode(@PathVariable String serviceCode,
                                                                                       @PathVariable SegmentRecordType segmentRecordType) {
        return segmentService.findServiceSegmentSubsegmentsByServiceCode(serviceCode,segmentRecordType);
    }

}
