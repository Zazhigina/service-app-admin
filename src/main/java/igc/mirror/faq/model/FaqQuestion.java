package igc.mirror.faq.model;

import igc.mirror.faq.ref.MirrorService;
import igc.mirror.faq.ref.QuestionType;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class FaqQuestion {
    private Long id;
    private MirrorService serviceName;
    private String name;
    private QuestionType questionType;

    private LocalDateTime createDate;
    private String createUser;
    private LocalDateTime lastUpdateDate;
    private String lastUpdateUser;

    /**
     * only for fetch data
     */
    private FaqAnswer answer;
}
