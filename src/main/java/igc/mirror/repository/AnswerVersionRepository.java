package igc.mirror.repository;

import igc.mirror.model.AnswerVersion;
import jooqdata.tables.TAnswerVersion;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class AnswerVersionRepository {
    @Autowired
    DSLContext dsl;

    /**
     * Находит все варианты ответов на указанный вопрос
     *
     * @return список ответов
     */
    public List<AnswerVersion> findAnswerVersionsByQuestion(Long questionId) {
        return dsl.selectFrom(TAnswerVersion.T_ANSWER_VERSION)
                .where(TAnswerVersion.T_ANSWER_VERSION.QUESTION_ID.equal(questionId))
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
        return dsl.selectFrom(TAnswerVersion.T_ANSWER_VERSION)
                .where(TAnswerVersion.T_ANSWER_VERSION.QUESTION_ID.eq(questionId)
                        .and(TAnswerVersion.T_ANSWER_VERSION.ORDER_NO.eq(orderNo)))
                .fetchOneInto(AnswerVersion.class);
    }

    /**
     * Обновляет вариант ответа в БД
     *
     * @param  answerVersion измененный вариант ответа
     */
    public void updateAnswerVersion(AnswerVersion answerVersion) {
        dsl.update(TAnswerVersion.T_ANSWER_VERSION)
                .set(TAnswerVersion.T_ANSWER_VERSION.NAME, answerVersion.getName())
                .set(TAnswerVersion.T_ANSWER_VERSION.IS_DEFAULT, answerVersion.isUsed())
                .set(TAnswerVersion.T_ANSWER_VERSION.LAST_UPDATE_USER, answerVersion.getLastUpdateUser())
                .where(TAnswerVersion.T_ANSWER_VERSION.ID.equal(answerVersion.getId()))
                .execute();
    }
}
