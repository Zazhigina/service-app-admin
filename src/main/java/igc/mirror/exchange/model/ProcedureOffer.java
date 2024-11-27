package igc.mirror.exchange.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProcedureOffer {
    private String contractorCode;
    private String contractorName;
    private String contractorEsuCode;
    private String inn;
    private String kpp;
    private String cost;
    private String contractDate;
    private String offerStatus;
}
