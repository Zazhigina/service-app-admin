package igc.mirror.version.ref;

public enum VersionProcessingStatus {
    NOT_PROCESSED("Не обработано"),
    PROCESSED("Обработано");

    private final String name;

    VersionProcessingStatus(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
