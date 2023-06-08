package igc.mirror.dto;

import igc.mirror.model.LetterTemplate;
import igc.mirror.model.LetterTemplateVariable;
import igc.mirror.ref.LetterTemplateType;
import igc.mirror.utils.validate.group.ChangeGroup;
import igc.mirror.utils.validate.group.CreateGroup;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;

import java.time.LocalDateTime;
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
    @NotNull(groups = {CreateGroup.class, ChangeGroup.class})
    private Map<String, String> variables;
    private Long letterSample;
    private String sampleName; //todo add property: size of sample
    private LocalDateTime sampleCreateDate;
//    private String createUser;
//    private LocalDateTime lastUpdateDate;
//    private String lastUpdateUser;

    public LetterTemplateDto() {
    }

//    public LetterTemplateDto(Long id,
//                             String letterType,
//                             String title,
//                             Long letterSample,
//                             LetterTemplateType typeTemplate,
//                             Map<String, String> variables) {
//                             LocalDateTime createDate,
//                             String createUser,
//                             LocalDateTime lastUpdateDate,
//                             String lastUpdateUser) {
//        this.id = id;
//        this.letterType = letterType;
//        this.title = title;
//        this.letterSample = letterSample;
//        this.typeTemplate = typeTemplate;
//        this.variables = variables;
//        this.createDate = createDate;
//        this.createUser = createUser;
//        this.lastUpdateDate = lastUpdateDate;
//        this.lastUpdateUser = lastUpdateUser;
//    }

//    public static LetterTemplateDto fromModel(LetterTemplate letterTemplate){
//        return new LetterTemplateDto(
//                letterTemplate.getId(),
//                letterTemplate.getLetterType(),
//                letterTemplate.getTitle(),
//                letterTemplate.getLetterSample(),
//                LetterTemplateType.getByName(letterTemplate.getTypeTemplate()),
//                letterTemplate.getCreateDate(),
//                letterTemplate.getCreateUser(),
//                letterTemplate.getLastUpdateDate(),
//                letterTemplate.getLastUpdateUser()
//        );
//    }

    public LetterTemplateDto(LetterTemplate letterTemplate) {
        this.id = letterTemplate.getId();
        this.letterType = letterTemplate.getLetterType();
        this.title = letterTemplate.getTitle();
        this.letterSample = letterTemplate.getLetterSample();
        this.typeTemplate = LetterTemplateType.getByName(letterTemplate.getTypeTemplate());
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

    public String getSampleName() {
        return sampleName;
    }

    public void setSampleName(String sampleName) {
        this.sampleName = sampleName;
    }

    public LocalDateTime getSampleCreateDate() {
        return sampleCreateDate;
    }

    public void setSampleCreateDate(LocalDateTime sampleCreateDate) {
        this.sampleCreateDate = sampleCreateDate;
    }

    //    public String getCreateUser() {
//        return createUser;
//    }

//    public void setCreateUser(String createUser) {
//        this.createUser = createUser;
//    }

//    public LocalDateTime getLastUpdateDate() {
//        return lastUpdateDate;
//    }

//    public void setLastUpdateDate(LocalDateTime lastUpdateDate) {
//        this.lastUpdateDate = lastUpdateDate;
//    }

//    public String getLastUpdateUser() {
//        return lastUpdateUser;
//    }

//    public void setLastUpdateUser(String lastUpdateUser) {
//        this.lastUpdateUser = lastUpdateUser;
//    }
}
