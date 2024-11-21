package igc.mirror.exchange.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProcedureData {
    private String source;
    private LocalDateTime syncDate;
    private String messageGuid;
    private List<ProcedureItem> items;
}
