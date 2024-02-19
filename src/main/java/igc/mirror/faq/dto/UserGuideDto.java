package igc.mirror.faq.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserGuideDto {
    private Long templateId;
    private String description;
    private DocumentInfoDto documentInfo;
}
