package igc.mirror.dto;

import javax.validation.constraints.NotBlank;

public class ParamCreationDto {
    @NotBlank(message = "ParamCreationDto.key is empty")
    private String key;
    @NotBlank(message = "ParamCreationDto.name is empty")
    private String name;
    @NotBlank(message = "ParamCreationDto.val is empty")
    private String val;

    public ParamCreationDto(String key, String name, String val) {
        this.key = key;
        this.name = name;
        this.val = val;
    }

    public ParamCreationDto() {
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
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
