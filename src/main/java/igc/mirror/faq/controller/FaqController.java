package igc.mirror.faq.controller;

import igc.mirror.exception.handler.SuccessInfo;
import igc.mirror.faq.dto.FaqQuestionDto;
import igc.mirror.faq.dto.FaqQuestionSaveDto;
import igc.mirror.faq.ref.dto.ReferenceDto;
import igc.mirror.faq.dto.UserGuideDto;
import igc.mirror.faq.filter.FaqQuestionSearchCriteria;
import igc.mirror.faq.ref.MirrorService;
import igc.mirror.faq.service.FaqService;
import igc.mirror.utils.qfilter.DataFilter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/faq")
@Tag(name = "FAQ")
public class FaqController {
    @Autowired
    private FaqService faqService;

    @GetMapping("/user-guides")
    @Operation(summary = "Информация об инструкциях пользователя, соответствующих сервису Effect.Mirror")
    public List<UserGuideDto> getUserGuides(@RequestParam MirrorService mirrorService){
        return faqService.getUserGuides(mirrorService);
    }

    @PostMapping("/question")
    @Operation(summary = "Создание FAQ вопроса")
    public FaqQuestionDto createQuestion(@RequestBody FaqQuestionSaveDto faqQuestionSaveDto){
        return faqService.createQuestion(faqQuestionSaveDto);
    }

    @PostMapping("/questions/filter")
    @Operation(summary = "Список FAQ вопросов")
    public Page<FaqQuestionDto> getQuestionsByFilters(@RequestBody DataFilter<FaqQuestionSearchCriteria> dataFilter, Pageable pageable){
        return faqService.findQuestionsByFilters(dataFilter, pageable);
    }

    @GetMapping("/questions/{id}")
    @Operation(summary = "Данные FAQ вопроса")
    public FaqQuestionDto getQuestion(@PathVariable Long id){
        return faqService.findQuestionById(id);
    }

    @PutMapping("/questions/{id}")
    @Operation(summary = "Изменение FAQ вопроса")
    public FaqQuestionDto changeQuestion(@PathVariable Long id, @RequestBody FaqQuestionSaveDto faqQuestionSaveDto){
        return faqService.changeQuestion(id, faqQuestionSaveDto);
    }

    @DeleteMapping("/questions/{id}")
    @Operation(summary = "Удаление FAQ вопроса")
    public SuccessInfo removeQuestion(@PathVariable Long id){
        faqService.removeQuestion(id);
        return new SuccessInfo("Операция выполнена");
    }
}
