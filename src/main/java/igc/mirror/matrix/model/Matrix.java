package igc.mirror.matrix.model;

import igc.mirror.matrix.dto.MatrixDTO;
import org.apache.commons.lang3.builder.EqualsBuilder;

import java.time.LocalDateTime;

public class Matrix {
    /**
     * Id записи
     */
    private Long id;

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

    /**
     * Дата создания
     */
    private LocalDateTime createDate;

    /**
     * Автор изменения
     */
    private String createUser;

    /**
     * Дата изменения
     */
    private LocalDateTime lastUpdateDate;

    /**
     * Автор последнего изменения
     */
    private String lastUpdateUser;

    public Matrix() {
        this.companyCode = "";
        this.orgCode = "";
        this.customerCode  = "";
        this.initiatorCode = "";
    }

    public boolean isBaseInfoNotEqual(Matrix matrix){
        return (! new EqualsBuilder().append(this.orgCode,matrix.getOrgCode())
                .append(this.customerCode, matrix.getCustomerCode())
                .append(this.initiatorCode, matrix.getInitiatorCode())
                .isEquals());
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCompanyCode(){
        return companyCode;
    }

    public void setCompanyCode(String companyCode) {
        if (companyCode == null)
            this.companyCode = "";
        else
            this.companyCode = companyCode;
    }

    public String getOrgCode() {
        return orgCode;
    }

    public void setOrgCode(String orgCode) {
        if (orgCode == null)
            this.orgCode = "";
        else
            this.orgCode = orgCode;
    }

    public String getCustomerCode() {
        return customerCode;
    }

    public void setCustomerCode(String customerCode) {
        if (customerCode == null)
            this.customerCode = "";
        else
            this.customerCode = customerCode;
    }

    public String getInitiatorCode() {
        return initiatorCode;
    }

    public void setInitiatorCode(String initiatorCode) {
        if (initiatorCode == null)
            this.initiatorCode = "";
        else
            this.initiatorCode = initiatorCode;
    }

    public LocalDateTime getCreateDate() {
        return createDate;
    }

    public String getCreateUser() {
        return createUser;
    }

    public LocalDateTime getLastUpdateDate() {
        return lastUpdateDate;
    }

    public String getLastUpdateUser() {
        return lastUpdateUser;
    }

    public void fillAuthInfo(String userName) {
        this.lastUpdateUser = userName;
        if (this.id == null) {
            this.createDate = LocalDateTime.now();
            this.createUser = userName;
            this.lastUpdateDate = LocalDateTime.now();
        } else {
            this.lastUpdateDate = LocalDateTime.now();
        }
    }
}
