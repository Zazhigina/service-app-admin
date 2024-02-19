package igc.mirror.faq.ref.service;

import igc.mirror.faq.ref.MirrorService;
import igc.mirror.faq.ref.QuestionType;
import igc.mirror.faq.ref.dto.ReferenceDto;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class FaqReferenceService {
    public List<ReferenceDto> getMirrorServices(){
        return Arrays.stream(MirrorService.values())
                .map(s -> new ReferenceDto(s.name(), s.getName()))
                .toList();
    }

    public List<ReferenceDto> getFaqQuestionTypes(){
        return Arrays.stream(QuestionType.values())
                .map(s -> new ReferenceDto(s.name(), s.getDescription()))
                .toList();
    }
}
