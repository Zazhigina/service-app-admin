package igc.mirror.dto;

import javax.validation.constraints.NotNull;
import java.util.List;

public class ParamRemovalListDto {
    @NotNull
    private List<String> paramKeys;

    public ParamRemovalListDto(List<String> paramKeys) {
        this.paramKeys = paramKeys;
    }

    public ParamRemovalListDto() {
    }

    public List<String> getParamKeys() {
        return paramKeys;
    }

    public void setParamKeys(List<String> paramKeys) {
        this.paramKeys = paramKeys;
    }
}
