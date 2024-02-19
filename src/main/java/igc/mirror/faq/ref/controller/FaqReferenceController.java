package igc.mirror.faq.ref.controller;

import igc.mirror.faq.ref.dto.ReferenceDto;
import igc.mirror.faq.ref.service.FaqReferenceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/faq/reference")
@Tag(name = "Справочники FAQ")
public class FaqReferenceController {
    @Autowired
    private FaqReferenceService faqReferenceService;

    @GetMapping("/mirror-services")
    @Operation(summary = "Список сервисов Effect.Mirror")
    public List<ReferenceDto> getMirrorServices(){
        return faqReferenceService.getMirrorServices();
    }

    @GetMapping("/question-types")
    @Operation(summary = "Список типов FAQ вопросов")
    public List<ReferenceDto> getFaqQuestionTypes(){
        return faqReferenceService.getFaqQuestionTypes();
    }
}
