package igc.mirror.matrix.dto;

import igc.mirror.matrix.model.Initiator;

public class InitiatorDTO {
    /**
     * Id инициатора
     */
    private Long id;

    /**
     * Код инициатора
     */
    private String initiatorCode;

    /**
     * Название инициатора
     */
    private String initiatorName;

    /**
     * Блок инициатора
     */
    private String initiatorBlock;

    /**
     * Код заказчика
     */
    private String customerCode;

    public InitiatorDTO(Long id, String initiatorCode, String initiatorName, String initiatorBlock, String customerCode) {
        this.id             = id;
        this.initiatorCode  = initiatorCode;
        this.initiatorName  = initiatorName;
        this.initiatorBlock = initiatorBlock;
        this.customerCode   = customerCode;
    }

    public InitiatorDTO(Initiator initiator) {
        this.initiatorCode  = initiator.getInitiatorCode();
        this.initiatorName  = initiator.getInitiatorName();
        this.initiatorBlock = initiator.getInitiatorBlock();
        this.customerCode   = initiator.getCustomerCode();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getInitiatorCode() {
        return initiatorCode;
    }

    public void setInitiatorCode(String initiatorCode) {
        this.initiatorCode = initiatorCode;
    }

    public String getInitiatorName() {
        return initiatorName;
    }

    public void setInitiatorName(String initiatorName) {
        this.initiatorName = initiatorName;
    }

    public String getInitiatorBlock() {
        return initiatorBlock;
    }

    public void setInitiatorBlock(String initiatorBlock) {
        this.initiatorBlock = initiatorBlock;
    }

    public String getCustomerCode() {
        return customerCode;
    }

    public void setCustomerCode(String customerCode) {
        this.customerCode = customerCode;
    }
}
