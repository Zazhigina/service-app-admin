package igc.mirror.template.filter;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class LetterTemplateFilter {

    private List<String> letterTypes;

}
