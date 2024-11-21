package igc.mirror.matrix.model;

public class Initiator {

    /**
     * Код инициатора
     **/
    private String initiatorCode;

    /**
     * Наименование инициатора
     **/
    private String initiatorName;

    /**
     * Наименование блока
     */
    private String initiatorBlock;

    /**
     * Код заказчика
     */
    private String customerCode;

    public Initiator(){}

    public String getInitiatorCode() {
        return initiatorCode;
    }

    public void setCode(String initiatorCode) {
        this.initiatorCode = initiatorCode;
    }

    public String getInitiatorName() {
        return initiatorName;
    }

    public void setName(String initiatorName) {
        this.initiatorName = initiatorName;
    }

    public String getInitiatorBlock() {
        return initiatorBlock;
    }

    public void setBlock(String initiatorBlock) {
        this.initiatorBlock = initiatorBlock;
    }

    public String getCustomerCode() {
        return customerCode;
    }

    public void setCustomerCode(String customerCode) {
        this.customerCode = customerCode;
    }
}
