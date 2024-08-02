package igc.mirror.prcat.dto;

public class PrcatServiceVersionDto {

    private Long id;
    private String newServiceCode;
    private String oldServiceCode;
    private Long oldSubsegmentId;
    private Long newSubsegmentId;

    public PrcatServiceVersionDto() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNewServiceCode() {
        return newServiceCode;
    }

    public void setNewServiceCode(String newServiceCode) {
        this.newServiceCode = newServiceCode;
    }

    public String getOldServiceCode() {
        return oldServiceCode;
    }

    public void setOldServiceCode(String oldServiceCode) {
        this.oldServiceCode = oldServiceCode;
    }

    public Long getOldSubsegmentId() {
        return oldSubsegmentId;
    }

    public void setOldSubsegmentId(Long oldSubsegmentId) {
        this.oldSubsegmentId = oldSubsegmentId;
    }

    public Long getNewSubsegmentId() {
        return newSubsegmentId;
    }

    public void setNewSubsegmentId(Long newSubsegmentId) {
        this.newSubsegmentId = newSubsegmentId;
    }
}
