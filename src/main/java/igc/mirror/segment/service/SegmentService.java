package igc.mirror.segment.service;

import igc.mirror.nsi.service.NSIService;
import igc.mirror.segment.ref.SegmentRecordType;
import igc.mirror.segment.view.SegmentDto;
import igc.mirror.segment.view.ServiceSegmentSubsegmentDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SegmentService {
    @Autowired
    private NSIService nsiService;

    public List<SegmentDto> getSegmentsByType(SegmentRecordType segmentRecordType) {
        return nsiService.getSegmentsByType(segmentRecordType);
    }

    public List<ServiceSegmentSubsegmentDto> findServiceSegmentSubsegmentsByServiceCode(String serviceCode, SegmentRecordType segmentRecordType) {
        return nsiService.findServiceSegmentSubsegmentsByServiceCode(serviceCode,segmentRecordType);
    }
}
