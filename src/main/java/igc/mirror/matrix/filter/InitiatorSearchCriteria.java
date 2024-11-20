package igc.mirror.matrix.filter;

import java.time.LocalDateTime;

public class InitiatorSearchCriteria extends MatrixSearchCriteria {
    private String customerCode;

    public InitiatorSearchCriteria(LocalDateTime fromDate, LocalDateTime toDate, String searchKey, String customerCode) {
        super(fromDate, toDate, searchKey);
        this.customerCode = customerCode;
    }

    public String getCustomerCode() {
        return customerCode;
    }

    public void setCustomerCode(String customerCode) {
        this.customerCode = customerCode;
    }
}
