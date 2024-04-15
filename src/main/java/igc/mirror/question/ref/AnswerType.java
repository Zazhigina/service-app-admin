package igc.mirror.question.ref;

public enum AnswerType {
    POSITIVE("Положительный"),
    NEGATIVE("Отрицательный"),
    GREEN("5"),
    LIGHT_GREEN("4"),
    YELLOW("3"),
    ORANGE("2"),
    RED("1"),
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
