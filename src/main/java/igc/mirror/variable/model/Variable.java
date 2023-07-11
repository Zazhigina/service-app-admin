package igc.mirror.variable.model;

import java.time.LocalDateTime;

public class Variable {

    /**
     * Идентификатор переменной
     */
    private Long id;

    /**
     * Имя переменной
     */
    private String name;

    /**
     * Описание переменной
     */
    private String description;

    /**
     * Значение переменной по умолчанию
     */
    private String defaultVal;

    /**
     * Дата создания переменной
     */
    private LocalDateTime createDate;

    /**
     * Имя пользователя, создавшего переменную
     */
    private String createUser;

    /**
     * Дата последнего изменения переменной
     */
    private LocalDateTime lastUpdateDate;

    /**
     * Имя пользователя, изменившего переменную
     */
    private String lastUpdateUser;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDefaultVal() {
        return defaultVal;
    }

    public void setDefaultVal(String defaultVal) {
        this.defaultVal = defaultVal;
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
