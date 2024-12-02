package igc.mirror.feedback.dto;

import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FeedbackDto {
    private Long id;
    private String fbThemeName;
    private String feedbackText;
    private String userFullname;
    private LocalDateTime createDate;
    private String createUser;
    private LocalDateTime lastUpdateDate;
    private String lastUpdateUser;
}
