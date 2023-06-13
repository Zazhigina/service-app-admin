package igc.mirror.dto;

import igc.mirror.model.LetterTemplate;
import igc.mirror.ref.LetterTemplateType;

public class TemplateBriefInfoDto {
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
     * Наименование вида шаблона
     */
    private String typeTemplateName;

    /**
     * ИД документа в сервисе документации
     */
    private Long letterSample;

    public TemplateBriefInfoDto(LetterTemplate letterTemplate) {
        this.id = letterTemplate.getId();
        this.letterType = letterTemplate.getLetterType();
        this.title = letterTemplate.getTitle();
        this.typeTemplateName = LetterTemplateType.valueOf(letterTemplate.getTypeTemplate()).name();
        this.letterSample = letterTemplate.getLetterSample();
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
}
