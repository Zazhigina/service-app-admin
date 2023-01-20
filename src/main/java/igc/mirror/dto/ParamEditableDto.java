package igc.mirror.dto;

import javax.validation.constraints.NotBlank;

public class ParamEditableDto {
    @NotBlank(message = "ParamEditableDto.name is empty")
    private String name;
    @NotBlank(message = "ParamEditableDto.val is empty")
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
