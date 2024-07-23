package igc.mirror.question.ref;

public enum QuestionAnnex {
    EMPTY("Нет требований (пусто)"),
    FILE("Файл"),
    FILE_REQUIRED("Файл необходим"),
    COMMENT("Комментарий");

    private final String name;

    QuestionAnnex(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override // При сохранении в БД использовать только этот метод. (!) Не использовать метод .name()
    public String toString() {
        return this.equals(QuestionAnnex.EMPTY) ? null : super.toString();
    }
}
