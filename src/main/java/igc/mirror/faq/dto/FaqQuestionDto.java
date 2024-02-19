package igc.mirror.faq.dto;

import igc.mirror.faq.ref.MirrorService;
import igc.mirror.faq.ref.QuestionType;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class FaqQuestionDto {
    private Long id;
    private String name;
    private QuestionType questionType;
    private String questionTypeName;
    private MirrorService serviceName;
    private String serviceFullName;

    private FaqAnswerDto answer;
}
