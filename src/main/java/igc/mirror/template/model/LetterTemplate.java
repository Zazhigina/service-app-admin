package igc.mirror.template.model;

import java.time.LocalDateTime;

public class LetterTemplate {
    private Long id;

    /**
     * Имя параметра, определяющее шаблон
     */
    private String letterType;

    /**
     * Заголовок шаблона
     */
    private String title;

    /**
     * Идентификатор файла шаблона
     */
    private Long letterSample;

    /**
     * Дата и время создания
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

    /**
     * Вид шаблона
     */
    private String typeTemplate;

    /**
     * Формат документа
     */
    private String acceptableDocumentFormat;

    /**
     * Статус шаблона
     */
    private String status;

    public LetterTemplate() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLetterType() {
        return letterType;
    }

    public void setLetterType(String letterType) {
        this.letterType = letterType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getLetterSample() {
        return letterSample;
    }

    public void setLetterSample(Long letterSample) {
        this.letterSample = letterSample;
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

    public String getTypeTemplate() {
        return typeTemplate;
    }

    public void setTypeTemplate(String typeTemplate) {
        this.typeTemplate = typeTemplate;
    }

    public String getAcceptableDocumentFormat() {
        return acceptableDocumentFormat;
    }

    public void setAcceptableDocumentFormat(String acceptableDocumentFormat) {
        this.acceptableDocumentFormat = acceptableDocumentFormat;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
