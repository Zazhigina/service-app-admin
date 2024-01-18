package igc.mirror.question.repository;

import igc.mirror.question.dto.QuestionDto;
import igc.mirror.question.dto.StandardQuestion;
import igc.mirror.question.model.Question;
import igc.mirror.question.ref.QuestionOwner;
import jooqdata.tables.TAnswerVersion;
import jooqdata.tables.TQuestion;
import org.jooq.*;
import org.jooq.impl.DSL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

import static org.jooq.impl.DSL.multiset;
import static org.jooq.impl.DSL.select;

@Repository
public class QuestionRepository {
    @Autowired
    DSLContext dsl;

    private static final TQuestion QUESTION = TQuestion.T_QUESTION;

    private static final TAnswerVersion ANSWER_VERSION = TAnswerVersion.T_ANSWER_VERSION;

    private Condition getConditionStandardQuestion(QuestionOwner owner) {
        Condition wherePhrase = DSL.noCondition();
        if (owner != null)
            wherePhrase = wherePhrase.and(QUESTION.OWNER.eq(owner.getCode()));
        return wherePhrase;
    }

    private Result<Record7<Long, String, Integer, String, LocalDateTime, String, Result<Record5<Long, String, Integer, Boolean, String>>>> findAllStandardQuestionsByOwner(QuestionOwner owner) {
        return dsl.select(QUESTION.ID,
                        QUESTION.NAME,
                        QUESTION.ORDER_NO,
                        QUESTION.CODE,
                        QUESTION.ACTUAL_TO,
                        QUESTION.OWNER,
                        multiset(
                                select(ANSWER_VERSION.ID,
                                        ANSWER_VERSION.NAME,
                                        ANSWER_VERSION.ORDER_NO,
                                        ANSWER_VERSION.IS_DEFAULT,
                                        ANSWER_VERSION.TYPE.as("answerType"))
                                        .from(ANSWER_VERSION)
                                        .where(ANSWER_VERSION.QUESTION_ID.eq(QUESTION.ID))).as("answerVersions"))
                .from(QUESTION)
                .where(getConditionStandardQuestion(owner))
                .fetch();
    }

    /**
     * Находит все вопросы в БД
     *
     * @return список вопросов
     */
    public List<QuestionDto> findAllQuestions() {
//        return dsl.select(QUESTION.NAME,
//                        QUESTION.ORDER_NO,
//                        QUESTION.ACTUAL_TO,
//                        QUESTION.CODE,
//                        QUESTION.OWNER,
//                        multiset(
//                                select(ANSWER_VERSION.NAME,
//                                        ANSWER_VERSION.ORDER_NO,
//                                        ANSWER_VERSION.IS_DEFAULT,
//                                        ANSWER_VERSION.TYPE.as("answerType"))
//                                        .from(ANSWER_VERSION)
//                                        .where(ANSWER_VERSION.QUESTION_ID.eq(QUESTION.ID))).as("answerVersions"))
//                .from(QUESTION)
//                .fetchInto(QuestionDto.class);
        return findAllStandardQuestionsByOwner(null)
                .into(QuestionDto.class);

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
    public List<StandardQuestion> findAllStandardQuestions(QuestionOwner owner) {
        return findAllStandardQuestionsByOwner(owner)
                .into(StandardQuestion.class);
    }
}
