package igc.mirror.faq.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DocumentInfoDto {
    private String name;
    private Long size;
    private String mimeType;
    private LocalDateTime createDate;
    private LocalDateTime lastUpdateDate;
}
