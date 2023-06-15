package igc.mirror.dto;

import igc.mirror.model.LetterTemplate;
import igc.mirror.model.LetterTemplateVariable;
import igc.mirror.ref.LetterTemplateType;
import igc.mirror.utils.validate.group.ChangeGroup;
import igc.mirror.utils.validate.group.CreateGroup;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;

import java.util.Map;
import java.util.stream.Collectors;

public class LetterTemplateDto {
    @Null(groups = {CreateGroup.class})
    private Long id;
    @NotBlank(groups = {CreateGroup.class, ChangeGroup.class})
    private String letterType;
    @NotBlank(groups = {CreateGroup.class, ChangeGroup.class})
    private String title;
    @NotNull(groups = {CreateGroup.class, ChangeGroup.class})
    private LetterTemplateType typeTemplate;
    private Map<String, String> variables;
    private Long letterSample;
    private FileDto sampleInfo;

    public LetterTemplateDto() {
    }

    public LetterTemplateDto(LetterTemplate letterTemplate) {
        this.id = letterTemplate.getId();
        this.letterType = letterTemplate.getLetterType();
        this.title = letterTemplate.getTitle();
        this.letterSample = letterTemplate.getLetterSample();
        this.typeTemplate = LetterTemplateType.valueOf(letterTemplate.getTypeTemplate());
        this.variables = letterTemplate.getVariables() != null
                ? letterTemplate.getVariables().stream().collect(Collectors.toMap(LetterTemplateVariable::getName, LetterTemplateVariable::getVal))
                : null;
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

    public Map<String, String> getVariables() {
        return variables;
    }

    public void setVariables(Map<String, String> variables) {
        this.variables = variables;
    }

    public Long getLetterSample() {
        return letterSample;
    }

    public void setLetterSample(Long letterSample) {
        this.letterSample = letterSample;
    }

    public FileDto getSampleInfo() {
        return sampleInfo;
    }

    public void setSampleInfo(FileDto sampleInfo) {
        this.sampleInfo = sampleInfo;
    }
}
