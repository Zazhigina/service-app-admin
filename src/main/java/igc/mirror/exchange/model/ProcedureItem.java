package igc.mirror.exchange.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProcedureItem {
    private String lotNr;
    private String lotId;
    private String lotName;
    private String procedureNr;
    private String procedureId;
    private String planCompletionDate;
    private String dateOfPublication;
    private String organizer;
    private String organizerCode;
    private String initiator;
    private String purchaseCategory;
    private String purchaseForm;
    private String serviceCode;
    private String customer;
    private String nmc;
    private String protocolDate;
    List<String> okved;
    List<String> okpd;
    List<String> okato;
    List<ProcedureOffer> offers;
}
