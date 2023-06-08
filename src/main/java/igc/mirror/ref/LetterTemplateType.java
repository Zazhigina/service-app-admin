package igc.mirror.ref;

public enum LetterTemplateType {
    DOCUMENT,
    TEMPLATE;

    private String name;

    LetterTemplateType() {}

//    LetterTemplateType(String name) {
//        this.name = name;
//    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public static void fill(String baseConst, String description) {
        LetterTemplateType letterTemplateType = LetterTemplateType .valueOf(baseConst);
        if (letterTemplateType != null && letterTemplateType.getName() == null){
            letterTemplateType.setName(description);
        }
    }
}
