package igc.mirror.segment.view;

public class ServiceSegmentSubsegmentDto {
    /**
     * Идентификатор сочетания
     */
    private Long id;
    private String serviceCode;
    private Long segmentId;
    private Long subsegmentId;
    private String segmentName;
    private String subsegmentName;

    public ServiceSegmentSubsegmentDto() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getServiceCode() {
        return serviceCode;
    }

    public void setServiceCode(String serviceCode) {
        this.serviceCode = serviceCode;
    }

    public Long getSegmentId() {
        return segmentId;
    }

    public void setSegmentId(Long segmentId) {
        this.segmentId = segmentId;
    }

    public Long getSubsegmentId() {
        return subsegmentId;
    }

    public void setSubsegmentId(Long subsegmentId) {
        this.subsegmentId = subsegmentId;
    }

    public String getSegmentName() {
        return segmentName;
    }

    public void setSegmentName(String segmentName) {
        this.segmentName = segmentName;
    }

    public String getSubsegmentName() {
        return subsegmentName;
    }

    public void setSubsegmentName(String subsegmentName) {
        this.subsegmentName = subsegmentName;
    }
}
