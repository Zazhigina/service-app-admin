package igc.mirror.question.ref;

public enum AnswerType {
    POSITIVE("Положительный"),
    NEGATIVE("Отрицательный"),
    GREEN("5 баллов"),
    LIGHT_GREEN("4 балла"),
    YELLOW("3 балла"),
    ORANGE("2 балла"),
    RED("1 балл"),
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
