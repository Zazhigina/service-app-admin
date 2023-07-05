package igc.mirror.question.repository;

import igc.mirror.question.dto.QuestionDto;
import igc.mirror.question.model.Question;
import jooqdata.tables.TAnswerVersion;
import jooqdata.tables.TQuestion;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

import static org.jooq.impl.DSL.multiset;
import static org.jooq.impl.DSL.select;

@Repository
public class QuestionRepository {
    @Autowired
    DSLContext dsl;

    /**
     * Находит все вопросы в БД
     *
     * @return список вопросов
     */
    public List<QuestionDto> findAllQuestions() {
        return dsl.select(TQuestion.T_QUESTION.NAME,
                        TQuestion.T_QUESTION.ORDER_NO,
                        TQuestion.T_QUESTION.ACTUAL_TO,
                        multiset(
                                select(TAnswerVersion.T_ANSWER_VERSION.NAME,
                                        TAnswerVersion.T_ANSWER_VERSION.ORDER_NO,
                                        TAnswerVersion.T_ANSWER_VERSION.IS_DEFAULT)
                                        .from(TAnswerVersion.T_ANSWER_VERSION)
                                        .where(TAnswerVersion.T_ANSWER_VERSION.QUESTION_ID.eq(TQuestion.T_QUESTION.ID))).as("answerVersions"))
                .from(TQuestion.T_QUESTION)
                .fetchInto(QuestionDto.class);
    }

    /**
     * Находит вопрос по порядковому номеру в БД
     *
     * @return вопрос
     */
    public Question findQuestionByOrderNo(Integer orderNo) {
        return dsl.selectFrom(TQuestion.T_QUESTION)
                .where((TQuestion.T_QUESTION.ORDER_NO.eq(orderNo)))
                .fetchOneInto(Question.class);
    }

    /**
     * Обновляет вопрос в БД
     *
     * @param question измененный вопрос
     */
    public void updateQuestion(Question question) {
        dsl.update(TQuestion.T_QUESTION)
                .set(TQuestion.T_QUESTION.NAME, question.getName())
                .set(TQuestion.T_QUESTION.ACTUAL_TO, question.getActualTo())
                .set(TQuestion.T_QUESTION.LAST_UPDATE_USER, question.getLastUpdateUser())
                .where(TQuestion.T_QUESTION.ID.equal(question.getId()))
                .execute();
    }
}
