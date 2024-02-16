package igc.mirror.segment.controller;

import igc.mirror.segment.ref.SegmentRecordType;
import igc.mirror.segment.service.SegmentService;
import igc.mirror.segment.view.SegmentDto;
import igc.mirror.segment.view.ServiceSegmentSubsegmentDto;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class SegmentController {
    @Autowired
    private SegmentService segmentService;

    @Operation(summary = "Справочник сегментов/подсегментов")
    @GetMapping(path = "/{segmentRecordType}")
    public List<SegmentDto> getSegmentsByType(@PathVariable SegmentRecordType segmentRecordType){
        return segmentService.getSegmentsByType(segmentRecordType);
    }

    @Operation(summary = "Актуальные сочетания услуга-сегмент-подсегмент")
    @GetMapping(path = "/service/{serviceCode}/{segmentRecordType}")
    public List<ServiceSegmentSubsegmentDto> getServiceSegmentSubsegmentsByServiceCode(@PathVariable String serviceCode,
                                                                                       @PathVariable SegmentRecordType segmentRecordType) {
        return segmentService.findServiceSegmentSubsegmentsByServiceCode(serviceCode,segmentRecordType);
    }

}
