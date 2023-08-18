package igc.mirror.question.ref;

public enum AnswerType {
    POSITIVE("Положительный"),
    NEGATIVE("Отрицательный"),
    ANY("Другой");

    private String name;

    AnswerType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
