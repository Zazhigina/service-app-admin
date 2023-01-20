package igc.mirror.dto;

import igc.mirror.model.Param;

import java.time.LocalDateTime;

public class ParamDto {
    private String key;
    private String name;
    private String val;
    private LocalDateTime createDate;
    private String createUser;
    private LocalDateTime lastUpdateDate;
    private String lastUpdateUser;

    public static ParamDto fromModel(Param paramModel){
        return new ParamDto(paramModel.getKey(), paramModel.getName(), paramModel.getVal(), paramModel.getCreateDate(),
                paramModel.getCreateUser(), paramModel.getLastUpdateDate(), paramModel.getLastUpdateUser());
    }

    public ParamDto(String key, String name, String val, LocalDateTime createDate, String createUser, LocalDateTime lastUpdateDate, String lastUpdateUser) {
        this.key = key;
        this.name = name;
        this.val = val;
        this.createDate = createDate;
        this.createUser = createUser;
        this.lastUpdateDate = lastUpdateDate;
        this.lastUpdateUser = lastUpdateUser;
    }

    public ParamDto() {
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVal() {
        return val;
    }

    public void setVal(String val) {
        this.val = val;
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
}
