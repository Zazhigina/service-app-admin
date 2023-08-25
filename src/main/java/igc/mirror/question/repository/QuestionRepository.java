package igc.mirror.question.repository;

import igc.mirror.question.dto.QuestionDto;
import igc.mirror.question.dto.StandardQuestion;
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

    private static final TQuestion QUESTION = TQuestion.T_QUESTION;

    private static final TAnswerVersion ANSWER_VERSION = TAnswerVersion.T_ANSWER_VERSION;

    /**
     * Находит все вопросы в БД
     *
     * @return список вопросов
     */
    public List<QuestionDto> findAllQuestions() {
        return dsl.select(QUESTION.NAME,
                        QUESTION.ORDER_NO,
                        QUESTION.ACTUAL_TO,
                        QUESTION.CODE,
                        multiset(
                                select(ANSWER_VERSION.NAME,
                                        ANSWER_VERSION.ORDER_NO,
                                        ANSWER_VERSION.IS_DEFAULT,
                                        ANSWER_VERSION.TYPE.as("answerType"))
                                        .from(ANSWER_VERSION)
                                        .where(ANSWER_VERSION.QUESTION_ID.eq(QUESTION.ID))).as("answerVersions"))
                .from(QUESTION)
                .fetchInto(QuestionDto.class);
    }

    /**
     * Находит вопрос по порядковому номеру в БД
     *
     * @return вопрос
     */
    public Question findQuestionByOrderNo(Integer orderNo) {
        return dsl.selectFrom(QUESTION)
                .where((QUESTION.ORDER_NO.eq(orderNo)))
                .fetchOneInto(Question.class);
    }

    /**
     * Обновляет вопрос в БД
     *
     * @param question измененный вопрос
     */
    public void updateQuestion(Question question) {
        dsl.update(QUESTION)
                .set(QUESTION.NAME, question.getName())
                .set(QUESTION.ACTUAL_TO, question.getActualTo())
                .set(QUESTION.LAST_UPDATE_USER, question.getLastUpdateUser())
                .where(QUESTION.ID.equal(question.getId()))
                .execute();
    }

    /**
     * Находит все стандартные вопросы в БД
     *
     * @return список стандартных вопросов
     */
    public List<StandardQuestion> findAllStandardQuestions() {
        return dsl.select(QUESTION.ID,
                        QUESTION.NAME,
                        QUESTION.ORDER_NO,
                        QUESTION.CODE,
                        multiset(
                                select(ANSWER_VERSION.ID,
                                        ANSWER_VERSION.NAME,
                                        ANSWER_VERSION.ORDER_NO,
                                        ANSWER_VERSION.IS_DEFAULT,
                                        ANSWER_VERSION.TYPE.as("answerType"))
                                        .from(ANSWER_VERSION)
                                        .where(ANSWER_VERSION.QUESTION_ID.eq(QUESTION.ID))).as("answerVersions"))
                .from(QUESTION)
                .fetchInto(StandardQuestion.class);
    }
}
