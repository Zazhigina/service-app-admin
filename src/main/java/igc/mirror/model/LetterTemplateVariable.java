package igc.mirror.model;

import java.time.LocalDateTime;

public class LetterTemplateVariable {
    private long id;

    /**
     * Идентификатор шаблона письма
     */
    private long letterTemplateId;

    /**
     * Имя переменной
     */
    private String name;

    /**
     * Значение переменной
     */
    private String val;

    /**
     * Дата и время создания документа
     */
    private LocalDateTime createDate;

    /**
     * Автор создания
     */
    private String createUser;

    /**
     * Дата и время последнего изменения
     */
    private LocalDateTime lastUpdateDate;

    /**
     * Автор изменения
     */
    private String lastUpdateUser;

    public LetterTemplateVariable() {}

    public LetterTemplateVariable(long letterTemplateId, String name, String val) {
        this.letterTemplateId = letterTemplateId;
        this.name = name;
        this.val = val;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getLetterTemplateId() {
        return letterTemplateId;
    }

    public void setLetterTemplateId(long letterTemplateId) {
        this.letterTemplateId = letterTemplateId;
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
