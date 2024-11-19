package igc.mirror.matrix.dto;

import igc.mirror.matrix.model.Customer;

public class CustomerDTO {
    /**
     * Id заказчика
     */
    private Long id;

    /**
     * Код заказчика
     */
    private String customerCode;

    /**
     * Название заказчика
     */
    private String customerName;

    public CustomerDTO(Long id, String customerCode, String customerName) {
        this.id           = id;
        this.customerCode = customerCode;
        this.customerName = customerName;
    }

    public CustomerDTO(Customer customer) {
        this.customerCode = customer.getCode();
        this.customerName = customer.getName();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCustomerCode() {
        return customerCode;
    }

    public void setCustomerCode(String customerCode) {
        this.customerCode = customerCode;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }
}
