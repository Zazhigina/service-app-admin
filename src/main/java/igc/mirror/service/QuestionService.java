package igc.mirror.service;

import igc.mirror.auth.UserDetails;
import igc.mirror.dto.AnswerVersionDto;
import igc.mirror.dto.QuestionDto;
import igc.mirror.model.AnswerVersion;
import igc.mirror.model.Question;
import igc.mirror.repository.AnswerVersionRepository;
import igc.mirror.repository.QuestionRepository;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.Optional;

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
        List<Question> questions = questionRepository.findAllQuestions();
        questions.forEach(question -> question.setAnswerVersions(answerVersionRepository.findAnswerVersionsByQuestion(question.getId())));

        return questions.stream().map(QuestionDto::new).toList();
    }

    /**
     * Изменяет данные стандартных вопросов и вариантов ответов
     *
     * @param questions список измененных вопросов
     */
    public void changeQuestions(@Valid List<QuestionDto> questions) {
        logger.info("Изменение перечня стандартных вопросов");
        for (QuestionDto question : questions) {
            Question currentQuestion = Optional.ofNullable(questionRepository.findQuestionByOrderNo(question.getOrderNo()))
                    .orElseThrow(() -> new igc.mirror.exception.common.EntityNotFoundException("Вопрос с порядковым номером "+ question.getOrderNo() +" не найден", null, Question.class));
            currentQuestion.setName(question.getName());
            currentQuestion.setActualTo(question.getActualTo());
            currentQuestion.setLastUpdateUser(userDetails.getUsername());
            questionRepository.updateQuestion(currentQuestion);

            for (AnswerVersionDto answerVersion : question.getAnswerVersions()) {
                AnswerVersion currentAnswerVersion = Optional.ofNullable(answerVersionRepository.findAnswerByOrderNo(currentQuestion.getId(), answerVersion.getOrderNo()))
                        .orElseThrow(() -> new igc.mirror.exception.common.EntityNotFoundException("Вариант ответа с порядковым номером "+ answerVersion.getOrderNo() +" не найден", null, AnswerVersion.class));
                currentAnswerVersion.setName(answerVersion.getName());
                currentAnswerVersion.setUsed(answerVersion.isUsed());
                currentAnswerVersion.setLastUpdateUser(userDetails.getUsername());
                answerVersionRepository.updateAnswerVersion(currentAnswerVersion);
            }
        }
    }
}
