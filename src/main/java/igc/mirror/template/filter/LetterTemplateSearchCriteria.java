package igc.mirror.template.filter;

import com.fasterxml.jackson.annotation.JsonIgnore;
import igc.mirror.template.ref.LetterTemplateType;
import igc.mirror.template.ref.TemplateStatus;
import igc.mirror.utils.qfilter.SearchCriteria;

import java.util.List;

public class LetterTemplateSearchCriteria extends SearchCriteria {
    private String letterType;
    private String title;
    private LetterTemplateType letterTemplateType;
    @JsonIgnore
    private TemplateStatus status;
    @JsonIgnore
    private List<String> letterTypeLikeList;

    public LetterTemplateSearchCriteria() {}
    public LetterTemplateSearchCriteria(String letterType, String title) {
        this.letterType = letterType;
        this.title = title;
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

    public LetterTemplateType getLetterTemplateType() {
        return letterTemplateType;
    }

    public void setLetterTemplateType(LetterTemplateType letterTemplateType) {
        this.letterTemplateType = letterTemplateType;
    }

    public TemplateStatus getStatus() {
        return status;
    }

    public void setStatus(TemplateStatus status) {
        this.status = status;
    }

    public List<String> getLetterTypeLikeList() {
        return letterTypeLikeList;
    }

    public void setLetterTypeLikeList(List<String> letterTypeLikeList) {
        this.letterTypeLikeList = letterTypeLikeList;
    }
}
