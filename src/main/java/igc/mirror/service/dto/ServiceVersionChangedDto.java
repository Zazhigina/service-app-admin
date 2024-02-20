package igc.mirror.service.dto;

import java.time.LocalDateTime;

public class ServiceVersionChangedDto {
    /**
     * Идентификатор записи
     */
    private Long id;

    /**
     * Новый код услуги и наименование
     */
    private String newServiceCode;

    /**
     * Старый код услуги и наименование
     */
    private String oldServiceCode;

    /**
     * Старый код сегмента
     */
    private Long oldSegmentId;

    /**
     * Старый код подсегмента
     */
    private Long oldSubsegmentId;

    /**
     * Новый код сегмента
     */
    private Long newSegmentId;

    /**
     * Новый код подсегмента
     */
    private Long newSubsegmentId;

    /**
     * Версия КТ-777
     */
    private String changeVersion;

    /**
     * Дата версии
     */
    private LocalDateTime versionDate;

    /**
     * Дата и время создания
     */
    private LocalDateTime createDate;

    /**
     * Автор создания
     */
    private String createUser;

    /**
     * Дата и время изменения
     */
    private LocalDateTime lastUpdateDate;

    /**
     * Автор изменения
     */
    private String lastUpdateUser;

    public ServiceVersionChangedDto() {

    }

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

    public String getChangeVersion() {
        return changeVersion;
    }

    public void setChangeVersion(String changeVersion) {
        this.changeVersion = changeVersion;
    }

    public LocalDateTime getVersionDate() {
        return versionDate;
    }

    public void setVersionDate(LocalDateTime versionDate) {
        this.versionDate = versionDate;
    }

    public LocalDateTime getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDateTime createDate) {
        this.createDate = createDate;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public LocalDateTime getLastUpdateDate() {
        return lastUpdateDate;
    }

    public void setLastUpdateDate(LocalDateTime lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }

    public String getLastUpdateUser() {
        return lastUpdateUser;
    }

    public void setLastUpdateUser(String lastUpdateUser) {
        this.lastUpdateUser = lastUpdateUser;
    }

    public Long getOldSegmentId() {
        return oldSegmentId;
    }

    public void setOldSegmentId(Long oldSegmentId) {
        this.oldSegmentId = oldSegmentId;
    }

    public Long getOldSubsegmentId() {
        return oldSubsegmentId;
    }

    public void setOldSubsegmentId(Long oldSubsegmentId) {
        this.oldSubsegmentId = oldSubsegmentId;
    }

    public Long getNewSegmentId() {
        return newSegmentId;
    }

    public void setNewSegmentId(Long newSegmentId) {
        this.newSegmentId = newSegmentId;
    }

    public Long getNewSubsegmentId() {
        return newSubsegmentId;
    }

    public void setNewSubsegmentId(Long newSubsegmentId) {
        this.newSubsegmentId = newSubsegmentId;
    }
}
