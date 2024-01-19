package igc.mirror.question.service;

import igc.mirror.auth.UserDetails;
import igc.mirror.question.dto.AnswerVersionDto;
import igc.mirror.question.dto.QuestionDto;
import igc.mirror.question.dto.StandardQuestion;
import igc.mirror.question.model.AnswerVersion;
import igc.mirror.question.model.Question;
import igc.mirror.question.ref.QuestionOwner;
import igc.mirror.question.repository.AnswerVersionRepository;
import igc.mirror.question.repository.QuestionRepository;
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

    private final QuestionRepository questionRepository;
    private final AnswerVersionRepository answerVersionRepository;
    private final UserDetails userDetails;

    @Autowired
    public QuestionService(QuestionRepository questionRepository, AnswerVersionRepository answerVersionRepository, UserDetails userDetails) {
        this.questionRepository = questionRepository;
        this.answerVersionRepository = answerVersionRepository;
        this.userDetails = userDetails;
    }

    /**
     * Получает перечень всех вопросов и вариантов ответов
     *
     * @return перечень вопросов
     */
    public List<QuestionDto> findAllQuestions() {
        logger.info("Получение всех вопросов");

        return questionRepository.findAllStandardQuestionsByOwner(null).into(QuestionDto.class);
    }

    /**
     * Изменяет данные вопросов и вариантов ответов
     *
     * @param questions список измененных вопросов
     */
    public void changeQuestions(@Valid List<QuestionDto> questions) {
        logger.trace("Изменение перечня вопросов");
        questions.forEach(this::changeQuestion);
    }

    private void changeQuestion(QuestionDto changedQuestionDto) {
        Optional<Question> optionalQuestion = questionRepository.findQuestionById(changedQuestionDto.getId());
        if (optionalQuestion.isPresent()) {
            Question currentQuestion = optionalQuestion.get();
            currentQuestion.setName(changedQuestionDto.getName());
            currentQuestion.setActualTo(changedQuestionDto.getActualTo());
            currentQuestion.setLastUpdateUser(userDetails.getUsername());
            questionRepository.updateQuestion(currentQuestion);

            changedQuestionDto.getAnswerVersions().forEach(this::changeAnswerVersion);
        }
    }

    private void changeAnswerVersion(AnswerVersionDto answerVersionDto) {
        Optional<AnswerVersion> optionalAnswerVersion = answerVersionRepository.findAnswerById(answerVersionDto.getId());
        if (optionalAnswerVersion.isPresent()) {
            AnswerVersion currentAnswerVersion = optionalAnswerVersion.get();
            currentAnswerVersion.setName(answerVersionDto.getName());
            currentAnswerVersion.setDefault(answerVersionDto.isDefault());
            currentAnswerVersion.setLastUpdateUser(userDetails.getUsername());
            answerVersionRepository.updateAnswerVersion(currentAnswerVersion);
        }
    }

    /**
     * Получает перечень стандартных вопросов и вариантов ответов
     *
     * @return перечень стандартных вопросов
     */
    public List<StandardQuestion> findAllStandardQuestions(QuestionOwner owner) {
        logger.trace("Получение перечня стандартных вопросов");

        return questionRepository.findAllStandardQuestionsByOwner(owner)
                .into(StandardQuestion.class);
    }
}
