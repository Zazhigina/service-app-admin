package igc.mirror.matrix.model;

public class Customer {

    /**
     * Код заказчика
     **/
    private String code;

    /**
     * Наименование заказчика
     **/
    private String name;

    public Customer() {}

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
