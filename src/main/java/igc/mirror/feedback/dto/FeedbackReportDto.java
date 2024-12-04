package igc.mirror.feedback.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FeedbackReportDto {
    private Long id;
    private String fbThemeName;
    private String feedbackText;
    private String userFullname;
    private LocalDateTime createDate;
    private String filename;
    private UUID uid;
}
