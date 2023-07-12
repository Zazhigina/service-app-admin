package igc.mirror.question.repository;

import igc.mirror.question.model.AnswerVersion;
import jooqdata.tables.TAnswerVersion;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class AnswerVersionRepository {
    @Autowired
    DSLContext dsl;

    private static final TAnswerVersion ANSWER_VERSION = TAnswerVersion.T_ANSWER_VERSION;

    /**
     * Находит все варианты ответов на указанный вопрос
     *
     * @return список ответов
     */
    public List<AnswerVersion> findAnswerVersionsByQuestion(Long questionId) {
        return dsl.selectFrom(ANSWER_VERSION)
                .where(ANSWER_VERSION.QUESTION_ID.equal(questionId))
                .fetchInto(AnswerVersion.class);
    }

    /**
     * Находит вариант ответа для указанного вопроса по порядковому номеру в БД
     *
     * @param  questionId идентификатор вопроса
     * @param  orderNo порядковый номер варианта ответа в запросе
     * @return вариант ответа
     */
    public AnswerVersion findAnswerByOrderNo(Long questionId, Integer orderNo) {
        return dsl.selectFrom(ANSWER_VERSION)
                .where(ANSWER_VERSION.QUESTION_ID.eq(questionId)
                        .and(ANSWER_VERSION.ORDER_NO.eq(orderNo)))
                .fetchOneInto(AnswerVersion.class);
    }

    /**
     * Обновляет вариант ответа в БД
     *
     * @param  answerVersion измененный вариант ответа
     */
    public void updateAnswerVersion(AnswerVersion answerVersion) {
        dsl.update(ANSWER_VERSION)
                .set(ANSWER_VERSION.NAME, answerVersion.getName())
                .set(ANSWER_VERSION.IS_DEFAULT, answerVersion.isDefault())
                .set(ANSWER_VERSION.LAST_UPDATE_USER, answerVersion.getLastUpdateUser())
                .where(ANSWER_VERSION.ID.equal(answerVersion.getId()))
                .execute();
    }
}
