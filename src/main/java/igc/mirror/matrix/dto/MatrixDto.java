package igc.mirror.matrix.dto;

import igc.mirror.matrix.model.Matrix;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Data
@AllArgsConstructor
@Getter
@Setter
public class MatrixDto {

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

    public MatrixDto(Matrix matrix) {
        this.companyCode   = matrix.getCompanyCode();
        this.orgCode       = matrix.getOrgCode();
        this.customerCode  = matrix.getCustomerCode();
        this.initiatorCode = matrix.getInitiatorCode();
    }
}
