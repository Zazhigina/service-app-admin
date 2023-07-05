package igc.mirror.param.dto;

import jakarta.validation.constraints.NotBlank;

public class ParamEditableDto {
    @NotBlank
    private String name;
    @NotBlank
    private String val;

    public ParamEditableDto(String name, String val) {
        this.name = name;
        this.val = val;
    }

    public ParamEditableDto() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVal() {
        return val;
    }

    public void setVal(String val) {
        this.val = val;
    }
}
