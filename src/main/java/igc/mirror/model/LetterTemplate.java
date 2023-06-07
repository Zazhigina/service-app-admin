package igc.mirror.model;

import java.time.LocalDateTime;
import java.util.List;

public class LetterTemplate { //extends TLetterTemplate {
    private long id;
    private String letterType;
    private String title;
    private Long letterSample;
    private List<LetterTemplateVariable> variables;
    private LocalDateTime createDate;
    private String createUser;
    private LocalDateTime lastUpdateDate;
    private String lastUpdateUser;
    private String typeTemplate;
    private String acceptableDocumentFormat;

    public LetterTemplate() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
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

    public List<LetterTemplateVariable> getVariables() {
        return variables;
    }

    public void setVariables(List<LetterTemplateVariable> variables) {
        this.variables = variables;
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

//    public LetterTemplate(Long id, String letterType, String title, Long letterSample, LocalDateTime createDate, String createUser, LocalDateTime lastUpdateDate, String lastUpdateUser, String typeTemplate, String acceptableDocumentFormat) {
//        super(id, letterType, title, letterSample, createDate, createUser, lastUpdateDate, lastUpdateUser, typeTemplate, acceptableDocumentFormat);
//    }
}
