package igc.mirror.coefficientsetting.ref;

public enum CoefficientCalculationType {
    PREVIOUS_YEAR_COEF("От предыдущего года"),
    PREVIOUS_YEAR_PERCENT("% от предыдущего года"),
    FIRST_YEAR_COEF("От первого года"),
    FIRST_YEAR_PERCENT("% от первого года");

    private final String name;

    CoefficientCalculationType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}