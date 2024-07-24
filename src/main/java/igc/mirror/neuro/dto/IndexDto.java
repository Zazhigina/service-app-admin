package igc.mirror.neuro.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class IndexDto {
    private String name;
    private Long size;

    public IndexDto(String name, Long size) {
        this.name = name;
        this.size = size;
    }
}
