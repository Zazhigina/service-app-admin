package igc.mirror.faq.filter;

import igc.mirror.faq.ref.MirrorService;
import igc.mirror.faq.ref.QuestionType;
import igc.mirror.utils.qfilter.SearchCriteria;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class FaqQuestionSearchCriteria extends SearchCriteria {
    private MirrorService mirrorService;
    private String name;
    private QuestionType questionType;
}
