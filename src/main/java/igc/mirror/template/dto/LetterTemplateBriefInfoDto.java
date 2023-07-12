package igc.mirror.template.dto;

import igc.mirror.template.ref.TemplateStatus;

public class LetterTemplateBriefInfoDto {
    private Long id;

    /**
     * Имя параметра
     */
    private String letterType;

    /**
     * Заголовок шаблона
     */
    private String title;

    /**
     * Вид шаблона
     */
    private String typeTemplate;

    /**
     * Наименование вида шаблона
     */
    private String typeTemplateName;

    /**
     * ИД документа в сервисе документации
     */
    private Long letterSample;

    /**
     * Статус шаблона
     */
    private TemplateStatus status;

    public LetterTemplateBriefInfoDto() {}

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

    public String getTypeTemplate() {
        return typeTemplate;
    }

    public void setTypeTemplate(String typeTemplate) {
        this.typeTemplate = typeTemplate;
    }

    public String getTypeTemplateName() {
        return typeTemplateName;
    }

    public void setTypeTemplateName(String typeTemplateName) {
        this.typeTemplateName = typeTemplateName;
    }

    public Long getLetterSample() {
        return letterSample;
    }

    public void setLetterSample(Long letterSample) {
        this.letterSample = letterSample;
    }

    public TemplateStatus getStatus() {
        return status;
    }

    public void setStatus(TemplateStatus status) {
        this.status = status;
    }
}
