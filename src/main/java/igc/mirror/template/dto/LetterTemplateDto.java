package igc.mirror.template.dto;

import igc.mirror.template.model.LetterTemplate;
import igc.mirror.template.ref.LetterTemplateType;
import igc.mirror.utils.validate.group.ChangeGroup;
import igc.mirror.utils.validate.group.CreateGroup;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;

import java.time.LocalDateTime;
import java.util.List;

public class LetterTemplateDto {
    @Null(groups = {CreateGroup.class})
    private Long id;
    @NotBlank(groups = {CreateGroup.class, ChangeGroup.class})
    private String letterType;
    @NotBlank(groups = {CreateGroup.class, ChangeGroup.class})
    private String title;
    @NotNull(groups = {CreateGroup.class, ChangeGroup.class})
    private LetterTemplateType typeTemplate;
    //private Map<String, String> variables;
    private List<Long> variableIds;
    private Long letterSample;
    private String sampleName;
    private Long sampleSize;
    private LocalDateTime sampleCreateDate;

    public LetterTemplateDto() {
    }

    public LetterTemplateDto(LetterTemplate letterTemplate) {
        this.id = letterTemplate.getId();
        this.letterType = letterTemplate.getLetterType();
        this.title = letterTemplate.getTitle();
        this.letterSample = letterTemplate.getLetterSample();
        this.typeTemplate = LetterTemplateType.valueOf(letterTemplate.getTypeTemplate());
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

    public LetterTemplateType getTypeTemplate() {
        return typeTemplate;
    }

    public void setTypeTemplate(LetterTemplateType typeTemplate) {
        this.typeTemplate = typeTemplate;
    }

//    public Map<String, String> getVariables() {
//        return variables;
//    }
//
//    public void setVariables(Map<String, String> variables) {
//        this.variables = variables;
//    }
    public List<Long> getVariableIds() {
        return variableIds;
    }

    public void setVariableIds(List<Long> variableIds) {
        this.variableIds = variableIds;
    }

    public Long getLetterSample() {
        return letterSample;
    }

    public void setLetterSample(Long letterSample) {
        this.letterSample = letterSample;
    }

    public String getSampleName() {
        return sampleName;
    }

    public void setSampleName(String sampleName) {
        this.sampleName = sampleName;
    }

    public Long getSampleSize() {
        return sampleSize;
    }

    public void setSampleSize(Long sampleSize) {
        this.sampleSize = sampleSize;
    }

    public LocalDateTime getSampleCreateDate() {
        return sampleCreateDate;
    }

    public void setSampleCreateDate(LocalDateTime sampleCreateDate) {
        this.sampleCreateDate = sampleCreateDate;
    }
}
