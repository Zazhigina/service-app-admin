package igc.mirror.question.ref;

public enum QuestionOwner {
    SERVICE_INTEREST_REQUEST("SERVICE_PRODUCT"), MATERIAL_INTEREST_REQUEST("MATERIAL");

    final String code;

    QuestionOwner(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
