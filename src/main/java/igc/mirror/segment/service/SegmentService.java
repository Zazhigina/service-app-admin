package igc.mirror.segment.service;

import igc.mirror.nsi.service.NSIService;
import igc.mirror.segment.ref.SegmentRecordType;
import igc.mirror.segment.view.SegmentDto;
import igc.mirror.segment.view.ServiceSegmentSubsegmentDto;
import igc.mirror.service.filter.SegmentSearchCriteria;
import igc.mirror.utils.qfilter.DataFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SegmentService {

    private final NSIService nsiService;

    public SegmentService(NSIService nsiService) {
        this.nsiService = nsiService;
    }

    public Page<SegmentDto> getSegmentsByFilter(DataFilter<SegmentSearchCriteria> filter, Pageable pageable) {
        return nsiService.getSegmentsByFilter(filter);
    }

    public List<ServiceSegmentSubsegmentDto> findServiceSegmentSubsegmentsByServiceCode(String serviceCode, SegmentRecordType segmentRecordType) {
        return nsiService.findServiceSegmentSubsegmentsByServiceCode(serviceCode,segmentRecordType);
    }
}
