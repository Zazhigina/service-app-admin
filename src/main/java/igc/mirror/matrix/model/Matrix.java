package igc.mirror.matrix.model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
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

    /**
     * Статус удаления
     */
    private Boolean isDeleted;

    public Matrix() {
        this.companyCode = "";
        this.orgCode = "";
        this.customerCode  = "";
        this.initiatorCode = "";
    }

    public boolean isBaseInfoNotEqual(Matrix matrix){
        return (! new EqualsBuilder().append(this.companyCode,matrix.getCompanyCode())
                .append(this.orgCode,matrix.getOrgCode())
                .append(this.customerCode, matrix.getCustomerCode())
                .append(this.initiatorCode, matrix.getInitiatorCode())
                .isEquals());
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
