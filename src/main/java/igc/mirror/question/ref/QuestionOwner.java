package igc.mirror.question.ref;

public enum QuestionOwner {
    SERVICE_PRODUCT("SERVICE_PRODUCT"), MATERIAL("MATERIAL");

    final String code;

    QuestionOwner(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
