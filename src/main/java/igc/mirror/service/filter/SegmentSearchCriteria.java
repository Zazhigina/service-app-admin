package igc.mirror.service.filter;

import igc.mirror.segment.ref.SegmentRecordType;
import igc.mirror.utils.qfilter.SearchCriteria;

public class SegmentSearchCriteria extends SearchCriteria {
    private String name;

    private SegmentRecordType type;

    public SegmentSearchCriteria() {

    }

    public SegmentSearchCriteria(String name, SegmentRecordType type) {
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public SegmentRecordType getType() {
        return type;
    }

    public void setType(SegmentRecordType type) {
        this.type = type;
    }
}
