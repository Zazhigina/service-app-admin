package igc.mirror.ref;

public enum LetterTemplateType {
    DOCUMENT("Документ"),
    TEMPLATE("Шаблон");

    private final String name;

    LetterTemplateType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static LetterTemplateType getByName(String name){
        LetterTemplateType result = null;

        for (LetterTemplateType letterTemplateType: LetterTemplateType.values()) {
            if(letterTemplateType.getName().equalsIgnoreCase(name)) {
                result = letterTemplateType;
                break;
            }
        }

        return result;
    }
}
