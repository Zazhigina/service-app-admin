package igc.mirror.question.service;

import igc.mirror.auth.UserDetails;
import igc.mirror.question.dto.AnswerVersionDto;
import igc.mirror.question.dto.QuestionDto;
import igc.mirror.question.model.AnswerVersion;
import igc.mirror.question.model.Question;
import igc.mirror.question.repository.AnswerVersionRepository;
import igc.mirror.question.repository.QuestionRepository;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Service
@Validated
public class QuestionService {

    static final Logger logger = LoggerFactory.getLogger(QuestionService.class);

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private AnswerVersionRepository answerVersionRepository;

    @Autowired
    private UserDetails userDetails;

    /**
     * Получает полный перечень стандартных вопросов и вариантов ответов
     *
     * @return перечень вопросов
     */
    public List<QuestionDto> findAllQuestions() {
        logger.info("Поучение перечня стандартных вопросов");

        return questionRepository.findAllQuestions();
    }

    /**
     * Изменяет данные стандартных вопросов и вариантов ответов
     *
     * @param questions список измененных вопросов
     */
    public void changeQuestions(@Valid List<QuestionDto> questions) {
        logger.info("Изменение перечня стандартных вопросов");
        for (QuestionDto question : questions) {
//            Question currentQuestion = Optional.ofNullable(questionRepository.findQuestionByOrderNo(question.getOrderNo()))
//                    .orElseThrow(() -> new igc.mirror.exception.common.EntityNotFoundException("Вопрос с порядковым номером " + question.getOrderNo() + " не найден", null, Question.class));
            Question currentQuestion = questionRepository.findQuestionByOrderNo(question.getOrderNo());
            if (currentQuestion == null) continue;

            currentQuestion.setName(question.getName());
            currentQuestion.setActualTo(question.getActualTo());
            currentQuestion.setLastUpdateUser(userDetails.getUsername());
            questionRepository.updateQuestion(currentQuestion);

            for (AnswerVersionDto answerVersion : question.getAnswerVersions()) {
//                AnswerVersion currentAnswerVersion = Optional.ofNullable(answerVersionRepository.findAnswerByOrderNo(currentQuestion.getId(), answerVersion.getOrderNo()))
//                        .orElseThrow(() -> new igc.mirror.exception.common.EntityNotFoundException("Вариант ответа с порядковым номером " + answerVersion.getOrderNo() + " не найден", null, AnswerVersion.class));
                AnswerVersion currentAnswerVersion = answerVersionRepository.findAnswerByOrderNo(currentQuestion.getId(), answerVersion.getOrderNo());
                if (currentAnswerVersion == null) continue;

                currentAnswerVersion.setName(answerVersion.getName());
                currentAnswerVersion.setDefault(answerVersion.isDefault());
                currentAnswerVersion.setLastUpdateUser(userDetails.getUsername());
                answerVersionRepository.updateAnswerVersion(currentAnswerVersion);
            }
        }
    }
}
