package igc.mirror.matrix.dto;

import igc.mirror.matrix.model.Matrix;

import java.util.List;

public class MatrixDTO {

    /**
     * Код компании организатора
     */
    private String companyCode;

    /**
     * Код организатора
     */
    private String orgCode;

    /**
     * Код заказчика
     */
    private String customerCode;

    /**
     * Код инициатора
     */
    private String initiatorCode;

    public MatrixDTO(String companyCode, String orgCode, String customerCode, String initiatorCode) {
        this.companyCode = companyCode;
        this.orgCode  = orgCode;
        this.customerCode = customerCode;
        this.initiatorCode  = initiatorCode;
    }

    public MatrixDTO(Matrix matrix) {
        this.companyCode = matrix.getCompanyCode();
        this.orgCode  = matrix.getOrgCode();
        this.customerCode = matrix.getCustomerCode();
        this.initiatorCode  = matrix.getInitiatorCode();
    }

    public String getCompanyCode(){
        return companyCode;
    }

    public void setCompanyCode(String companyCode) {
         this.companyCode = companyCode;
    }

    public String getOrgCode() {
        return orgCode;
    }

    public void setOrgCode(String orgCode) {
        this.orgCode = orgCode;
    }

    public String getCustomerCode() {
        return customerCode;
    }

    public void setCustomerCode(String customerCode) {
        this.customerCode = customerCode;
    }

    public void setInitiatorCode(String initiatorCode){
        this.initiatorCode = initiatorCode;
    }

    public String getInitiatorCode() {
        return initiatorCode;
    }
}
