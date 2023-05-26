package igc.mirror.dto;

import igc.mirror.model.LetterTemplate;
import igc.mirror.ref.LetterTemplateType;
import igc.mirror.utils.validate.group.ChangeGroup;
import igc.mirror.utils.validate.group.CreateGroup;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;

import java.time.LocalDateTime;

public class LetterTemplateDto {
    @Null(groups = {CreateGroup.class})
    private Long id;
    @NotBlank(groups = {CreateGroup.class})
    private String letterType;
    @NotBlank(groups = {CreateGroup.class, ChangeGroup.class})
    private String title;
    private Long letterSample;
    @NotNull(groups = {CreateGroup.class, ChangeGroup.class})
    private LetterTemplateType typeTemplate;
    private LocalDateTime createDate;
    private String createUser;
    private LocalDateTime lastUpdateDate;
    private String lastUpdateUser;

    public LetterTemplateDto() {
    }

    public LetterTemplateDto(Long id,
                             String letterType,
                             String title,
                             Long letterSample,
                             LetterTemplateType typeTemplate,
                             LocalDateTime createDate,
                             String createUser,
                             LocalDateTime lastUpdateDate,
                             String lastUpdateUser) {
        this.id = id;
        this.letterType = letterType;
        this.title = title;
        this.letterSample = letterSample;
        this.typeTemplate = typeTemplate;
        this.createDate = createDate;
        this.createUser = createUser;
        this.lastUpdateDate = lastUpdateDate;
        this.lastUpdateUser = lastUpdateUser;
    }

    public static LetterTemplateDto fromModel(LetterTemplate letterTemplate){
        return new LetterTemplateDto(
                letterTemplate.getId(),
                letterTemplate.getLetterType(),
                letterTemplate.getTitle(),
                letterTemplate.getLetterSample(),
                LetterTemplateType.getByName(letterTemplate.getTypeTemplate()),
                letterTemplate.getCreateDate(),
                letterTemplate.getCreateUser(),
                letterTemplate.getLastUpdateDate(),
                letterTemplate.getLastUpdateUser()
        );
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

    public LetterTemplateType getTypeTemplate() {
        return typeTemplate;
    }

    public void setTypeTemplate(LetterTemplateType typeTemplate) {
        this.typeTemplate = typeTemplate;
    }
}
