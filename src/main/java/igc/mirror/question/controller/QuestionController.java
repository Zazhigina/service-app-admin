package igc.mirror.question.controller;

import igc.mirror.exception.handler.SuccessInfo;
import igc.mirror.question.dto.QuestionDto;
import igc.mirror.question.dto.StandardQuestionDto;
import igc.mirror.question.service.QuestionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Стандартные вопросы")
public class QuestionController {

    @Autowired
    private QuestionService questionService;

    @GetMapping("/question")
    @Operation(summary = "Получить полный перечень вопросов")
    public List<QuestionDto> findAllQuestions() {
        return questionService.findAllQuestions();
    }

    @PutMapping("/question")
    @Operation(summary = "Изменение данных вопросов")
    @PreAuthorize("hasAuthority('APP_ADMIN.EXEC')")
    public ResponseEntity<SuccessInfo> changeQuestions(@RequestBody List<QuestionDto> questions) {
        questionService.changeQuestions(questions);
        return ResponseEntity.ok(new SuccessInfo("Операция выполнена успешно"));
    }

    @GetMapping("/standard-question")
    @Operation(summary = "Получить перечень стандартных вопросов")
    public List<StandardQuestionDto> findAllStandardQuestions() {
        return questionService.findAllStandardQuestions();
    }
}
