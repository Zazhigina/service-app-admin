package igc.mirror.service.ref;

public enum OfferType {
    COMMON("Общий профиль"),
    AUTO_TRANSPORT("Транспортные услуги"),
    IT_LICENSE("ИТ-лицензии"),
    SIMPLE("Профиль не определен"),
    SHANK("Хвостовики");

    private final String name;

    OfferType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
