package igc.mirror.exchange.model;

import igc.mirror.exchange.dto.ExternalSourceDto;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ExternalSource {
    private Long id;
    private String code;
    private String name;
    private String description;
    private boolean deleted;
    private LocalDateTime createDate;
    private String createUser;
    private LocalDateTime lastUpdateDate;
    private String lastUpdateUser;

    public ExternalSource(ExternalSourceDto recordDto) {
        this(
                recordDto.id(),
                recordDto.code(),
                recordDto.name(),
                recordDto.description(),
                recordDto.deleted(),
                null,
                null,
                null,
                null
        );
    }
}
