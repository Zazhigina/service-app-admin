package igc.mirror.service.dto;

import java.time.LocalDateTime;

public class ServiceVersionDTO {
    /**
     * Идентификатор записи
     */
    private Long id;

    /**
     * Новый код услуги и наименование
     */
    private String newServiceCode;
    private String newServiceName;

    /**
     * Старый код услуги и наименование
     */
    private String oldServiceCode;
    private String oldServiceName;

    /**
     * Старый код сегмента
     */
    private Long oldSegmentCode;

    /**
     * Старый код подсегмента
     */
    private Long oldSubsegmentCode;

    /**
     * Новый код сегмента
     */
    private Long newSegmentCode;

    /**
     * Новый код подсегмента
     */
    private Long newSubsegmentCode;

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

    public ServiceVersionDTO() {

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

    public String getNewServiceName() {
        return newServiceName;
    }

    public void setNewServiceName(String newServiceName) {
        this.newServiceName = newServiceName;
    }

    public String getOldServiceName() {
        return oldServiceName;
    }

    public void setOldServiceName(String oldServiceName) {
        this.oldServiceName = oldServiceName;
    }

    public Long getOldSegmentCode() {
        return oldSegmentCode;
    }

    public void setOldSegmentCode(Long oldSegmentCode) {
        this.oldSegmentCode = oldSegmentCode;
    }

    public Long getOldSubsegmentCode() {
        return oldSubsegmentCode;
    }

    public void setOldSubsegmentCode(Long oldSubsegmentCode) {
        this.oldSubsegmentCode = oldSubsegmentCode;
    }

    public Long getNewSegmentCode() {
        return newSegmentCode;
    }

    public void setNewSegmentCode(Long newSegmentCode) {
        this.newSegmentCode = newSegmentCode;
    }

    public Long getNewSubsegmentCode() {
        return newSubsegmentCode;
    }

    public void setNewSubsegmentCode(Long newSubsegmentCode) {
        this.newSubsegmentCode = newSubsegmentCode;
    }
}
