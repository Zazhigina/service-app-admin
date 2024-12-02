package igc.mirror.feedback.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FeedbackFileDto {
    private UUID uid;
    private Long feedbackId;
    private Long documentId;
    private String filename;
    private LocalDateTime createDate;
    private String createUser;
    private LocalDateTime lastUpdateDate;
    private String lastUpdateUser;

    public FeedbackFileDto(Long feedbackId, Long documentId, String filename, String createUser) {
        this.feedbackId = feedbackId;
        this.documentId = documentId;
        this.filename = filename;
        this.createUser = createUser;
    }
}
