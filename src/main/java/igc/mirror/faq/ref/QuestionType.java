package igc.mirror.faq.ref;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum QuestionType {
    GENERAL_QUESTIONS("Общие вопросы"),
    ADDITIONAL_AGREEMENTS("Дополнительные соглашения");

    private final String description;
}
