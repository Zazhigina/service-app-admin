package igc.mirror.question.repository;

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
import java.util.Optional;

import static org.jooq.impl.DSL.multiset;
import static org.jooq.impl.DSL.select;

@Repository
public class QuestionRepository {
    private static final TQuestion QUESTION = TQuestion.T_QUESTION;
    private static final TAnswerVersion ANSWER_VERSION = TAnswerVersion.T_ANSWER_VERSION;

    DSLContext dsl;

    @Autowired
    public QuestionRepository(DSLContext dsl) {
        this.dsl = dsl;
    }

    private Condition getConditionStandardQuestion(QuestionOwner owner) {
        Condition wherePhrase = DSL.noCondition();
        if (owner != null){
            //временное решение, после переноса убрать
            String str = owner.equals(QuestionOwner.SERVICE_INTEREST_REQUEST)? "SERVICE_PRODUCT" : "";
            wherePhrase = wherePhrase.and(QUESTION.OWNER.eq(String.valueOf(owner)).or(QUESTION.OWNER.eq(str)));
        }
        return wherePhrase;
    }

    /**
     * Находит все вопросы в БД
     *
     * @return список вопросов
     */
    public Result<Record8<Long, String, Integer, String, LocalDateTime, String, String, Result<Record5<Long, String, Integer, Boolean, String>>>> findAllStandardQuestionsByOwner(QuestionOwner owner) {
        return dsl.select(QUESTION.ID,
                        QUESTION.NAME,
                        QUESTION.ORDER_NO,
                        QUESTION.CODE,
                        QUESTION.ACTUAL_TO,
                        QUESTION.OWNER,
                        QUESTION.ANNEX,
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
     * Поиск вопроса по его id
     * @param id Идентификатор вопроса
     * @return Вопрос
     */
    public Optional<Question> findQuestionById(Long id) {
        return dsl.fetchOptional(QUESTION, QUESTION.ID.eq(id)).map(r -> r.into(Question.class));
    }

    /**
     * Поиск вопроса и его ответов по id
     * @param id Идентификатор вопроса
     * @return Вопрос с ответами
     */
    public StandardQuestion findStandardQuestionById(Long id) {
        return dsl.select(QUESTION.ID,
                        QUESTION.NAME,
                        QUESTION.ORDER_NO,
                        QUESTION.CODE,
                        QUESTION.ACTUAL_TO,
                        QUESTION.OWNER,
                        QUESTION.ANNEX,
                        multiset(
                                select(ANSWER_VERSION.ID,
                                        ANSWER_VERSION.NAME,
                                        ANSWER_VERSION.ORDER_NO,
                                        ANSWER_VERSION.IS_DEFAULT,
                                        ANSWER_VERSION.TYPE.as("answerType"))
                                        .from(ANSWER_VERSION)
                                        .where(ANSWER_VERSION.QUESTION_ID.eq(QUESTION.ID))).as("answerVersions"))
                .from(QUESTION)
                .where(QUESTION.ID.equal(id))
                .fetchAnyInto(StandardQuestion.class);
    }

    /**
     * Находит вопрос по порядковому номеру в БД
     *
     * @return вопрос
     */
    public Question findQuestionByOrderNo(Integer orderNo) {
        return dsl.fetchOptional(QUESTION, QUESTION.ORDER_NO.eq(orderNo)).map(r -> r.into(Question.class)).orElse(null);
    }

    /**
     * Обновляет вопрос в БД
     *
     * @param question измененный вопрос
     */
    public void updateQuestion(Question question) {
        dsl.update(QUESTION)
                .set(QUESTION.NAME, question.getName())
                .set(QUESTION.ANNEX, question.getAnnex() == null ? null : question.getAnnex().toString())
                .set(QUESTION.ACTUAL_TO, question.getActualTo())
                .set(QUESTION.LAST_UPDATE_USER, question.getLastUpdateUser())
                .where(QUESTION.ID.equal(question.getId()))
                .execute();
    }
}
