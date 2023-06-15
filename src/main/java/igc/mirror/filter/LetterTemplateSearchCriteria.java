package igc.mirror.filter;

import igc.mirror.utils.qfilter.SearchCriteria;

public class LetterTemplateSearchCriteria extends SearchCriteria {
    private String letterType;
    private String title;

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
}
