package igc.mirror.template.dto;

import igc.mirror.template.ref.TemplateStatus;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class LetterTemplateInfoDto {

    private String letterType;
    private String title;
    private String typeTemplate;
    private String typeTemplateName;
    private Long letterSample;
    private TemplateStatus status;
    private LocalDateTime lastUpdateDate;

}
