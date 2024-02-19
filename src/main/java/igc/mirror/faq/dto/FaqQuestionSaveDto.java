package igc.mirror.faq.dto;

import igc.mirror.faq.ref.MirrorService;
import igc.mirror.faq.ref.QuestionType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class FaqQuestionSaveDto {
    @NotBlank(message = "Текст вопроса должен быть заполнен")
    private String name;
    @NotNull(message = "Тип вопроса должен быть заполнен")
    private QuestionType questionType;
    @NotNull(message = "Сервис должен быть заполнен")
    private MirrorService serviceName;

    @NotNull(message = "Ответ на вопрос должен быть заполнен")
    private @Valid FaqAnswerSaveDto answer;
}
