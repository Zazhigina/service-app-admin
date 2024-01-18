package igc.mirror.param.model;

import java.time.LocalDateTime;
import java.util.Objects;

public class Param /*extends AParam */ {

    private String key;
    private String name;
    private String val;
    private LocalDateTime createDate;
    private String createUser;
    private LocalDateTime lastUpdateDate;
    private String lastUpdateUser;
    private String defaultVal;

    public Param() {

    }

    public Param(String key, String name, String val) {

        this.key = key;
        this.name = name;
        this.val = val;
    }

    public Param(String key, String name, String val, String defaultValue) {

        this.key = key;
        this.name = name;
        this.val = val;
        this.defaultVal = defaultValue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Param param = (Param) o;
        return Objects.equals(key, param.key) && Objects.equals(name, param.name) && Objects.equals(val, param.val) && Objects.equals(createDate, param.createDate) && Objects.equals(createUser, param.createUser) && Objects.equals(lastUpdateDate, param.lastUpdateDate) && Objects.equals(lastUpdateUser, param.lastUpdateUser) && Objects.equals(defaultVal, param.defaultVal);
    }

    @Override
    public int hashCode() {
        return Objects.hash(key, name, val, createDate, createUser, lastUpdateDate, lastUpdateUser, defaultVal);
    }

    @Override
    public String toString() {
        return "Param{" +
                "key='" + key + '\'' +
                ", name='" + name + '\'' +
                ", val='" + val + '\'' +
                ", createDate=" + createDate +
                ", createUser='" + createUser + '\'' +
                ", lastUpdateDate=" + lastUpdateDate +
                ", lastUpdateUser='" + lastUpdateUser + '\'' +
                ", defaultVal='" + defaultVal + '\'' +
                '}';
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

    public String getDefaultVal() {
        return defaultVal;
    }

    public void setDefaultVal(String defaultVal) {
        this.defaultVal = defaultVal;
    }
}
