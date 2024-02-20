package igc.mirror.service.dto;

import java.time.LocalDateTime;

public class ServiceVersionReadDto {
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
    private String oldServiceCatalog;

    /**
     * Старый код сегмента
     */
    private Long oldSegmentId;
    private String oldSegmentName;

    /**
     * Старый код подсегмента
     */
    private Long oldSubsegmentId;
    private String oldSubsegmentName;

    /**
     * Новый код сегмента
     */
    private Long newSegmentId;
    private String newSegmentName;

    /**
     * Новый код подсегмента
     */
    private Long newSubsegmentId;
    private String newSubsegmentName;

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

    public ServiceVersionReadDto() {

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

    public String getOldServiceCatalog() {
        return oldServiceCatalog;
    }

    public void setOldServiceCatalog(String oldServiceCatalog) {
        this.oldServiceCatalog = oldServiceCatalog;
    }

    public Long getOldSegmentId() {
        return oldSegmentId;
    }

    public void setOldSegmentId(Long oldSegmentId) {
        this.oldSegmentId = oldSegmentId;
    }

    public String getOldSegmentName() {
        return oldSegmentName;
    }

    public void setOldSegmentName(String oldSegmentName) {
        this.oldSegmentName = oldSegmentName;
    }

    public Long getOldSubsegmentId() {
        return oldSubsegmentId;
    }

    public void setOldSubsegmentId(Long oldSubsegmentId) {
        this.oldSubsegmentId = oldSubsegmentId;
    }

    public String getOldSubsegmentName() {
        return oldSubsegmentName;
    }

    public void setOldSubsegmentName(String oldSubsegmentName) {
        this.oldSubsegmentName = oldSubsegmentName;
    }

    public Long getNewSegmentId() {
        return newSegmentId;
    }

    public void setNewSegmentId(Long newSegmentId) {
        this.newSegmentId = newSegmentId;
    }

    public String getNewSegmentName() {
        return newSegmentName;
    }

    public void setNewSegmentName(String newSegmentName) {
        this.newSegmentName = newSegmentName;
    }

    public Long getNewSubsegmentId() {
        return newSubsegmentId;
    }

    public void setNewSubsegmentId(Long newSubsegmentId) {
        this.newSubsegmentId = newSubsegmentId;
    }

    public String getNewSubsegmentName() {
        return newSubsegmentName;
    }

    public void setNewSubsegmentName(String newSubsegmentName) {
        this.newSubsegmentName = newSubsegmentName;
    }
}
