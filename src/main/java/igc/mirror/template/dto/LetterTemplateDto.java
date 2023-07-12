package igc.mirror.template.dto;

import igc.mirror.template.model.LetterTemplate;
import igc.mirror.template.ref.LetterTemplateType;
import igc.mirror.template.ref.TemplateStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public class LetterTemplateDto {

    /**
     * Имя параметра, определяющее шаблон
     */
    @NotBlank(message = "Имя параметра, определяющего шаблон, не может быть пустым")
    private String letterType;

    /**
     * Вид шаблона
     */
    @NotNull(message = "Вид шаблона не может быть пустым")
    private LetterTemplateType typeTemplate;

    /**
     * Заголовок шаблона
     */
    private String title;

    /**
     * Идентификаторы переменных, определенных для шаблона
     */
    private List<Long> variableIds;

    /**
     * Информация о файле шаблона
     */
    private FileInfoDto fileInfo;

    /**
     * Статус шаблона
     */
    private TemplateStatus status;

    public LetterTemplateDto() {
    }
    public LetterTemplateDto(LetterTemplate letterTemplate) {
        this.letterType = letterTemplate.getLetterType();
        this.title = letterTemplate.getTitle();
        this.typeTemplate = letterTemplate.getTypeTemplate() != null ? LetterTemplateType.valueOf(letterTemplate.getTypeTemplate()) : null;
        this.status = letterTemplate.getStatus() != null ? TemplateStatus.valueOf(letterTemplate.getStatus()) : null;
    }

    public LetterTemplateDto(LetterTemplate letterTemplate, FileInfoDto fileInfo) {
        //this.id = letterTemplate.getId();
        this.letterType = letterTemplate.getLetterType();
        this.title = letterTemplate.getTitle();
        this.fileInfo = fileInfo;
        this.typeTemplate = letterTemplate.getTypeTemplate() != null ? LetterTemplateType.valueOf(letterTemplate.getTypeTemplate()) : null;
        this.status = letterTemplate.getStatus() != null ? TemplateStatus.valueOf(letterTemplate.getStatus()) : null;
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

    public LetterTemplateType getTypeTemplate() {
        return typeTemplate;
    }

    public void setTypeTemplate(LetterTemplateType typeTemplate) {
        this.typeTemplate = typeTemplate;
    }

    public List<Long> getVariableIds() {
        return variableIds;
    }

    public void setVariableIds(List<Long> variableIds) {
        this.variableIds = variableIds;
    }

    public FileInfoDto getFileInfo() {
        return fileInfo;
    }

    public void setFileInfo(FileInfoDto fileInfo) {
        this.fileInfo = fileInfo;
    }

    public TemplateStatus getStatus() {
        return status;
    }

    public void setStatus(TemplateStatus status) {
        this.status = status;
    }
}
