package igc.mirror.coefficientsetting.ref;

public enum CalculationMethod {
    MARKET_ANALYSIS(10, "Анализ рынка (запрос коммерческих предложений)"),
    COST(20, "Затратный метод"),
    EXECUTED_CONTRACT(30, "Анализ рынка (метод исполненных контрактов)"),
    COMBO_MARKET_ANALYSIS(40, "Анализ рынка (смешанный метод)");

    private final Integer code;
    private final String name;

    CalculationMethod(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    public Integer getCode() {
        return code;
    }

    public String getName() {
        return name;
    }
}