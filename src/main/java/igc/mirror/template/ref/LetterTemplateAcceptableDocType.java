package igc.mirror.template.ref;

public enum LetterTemplateAcceptableDocType {
    DOC("doc", "application/msword"),
    DOCX("docx", "application/vnd.openxmlformats-officedocument.wordprocessingml.document"),
    HTML("html", "text/html"),
    PDF("pdf", "application/pdf");

    private final String extension;
    private final String contentType;

    LetterTemplateAcceptableDocType(String extension, String contentType) {
        this.extension = extension;
        this.contentType = contentType;
    }

    public String getExtension() {
        return extension;
    }

    public String getContentType() {
        return contentType;
    }

    public static LetterTemplateAcceptableDocType getByExtension(String extension){
        LetterTemplateAcceptableDocType result = null;

        for (LetterTemplateAcceptableDocType letterTemplateAcceptableDocType: LetterTemplateAcceptableDocType.values()) {
            if(letterTemplateAcceptableDocType.getExtension().equalsIgnoreCase(extension)) {
                result = letterTemplateAcceptableDocType;
                break;
            }
        }

        return result;
    }

    public static LetterTemplateAcceptableDocType getByContentType(String contentType){
        LetterTemplateAcceptableDocType result = null;

        for (LetterTemplateAcceptableDocType letterTemplateAcceptableDocType: LetterTemplateAcceptableDocType.values()) {
            if(letterTemplateAcceptableDocType.getContentType().equalsIgnoreCase(contentType)) {
                result = letterTemplateAcceptableDocType;
                break;
            }
        }

        return result;
    }
}
