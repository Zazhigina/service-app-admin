package igc.mirror.faq.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class FaqAnswerSaveDto {
    @NotBlank(message = "Ответ на вопрос должен быть заполнен")
    private String name;
}
