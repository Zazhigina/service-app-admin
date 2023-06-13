package igc.mirror.repository;

import igc.mirror.model.Question;
import jooqdata.tables.TQuestion;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class QuestionRepository {
    @Autowired
    DSLContext dsl;

    /**
     * Находит все вопросы в БД
     *
     * @return список вопросов
     */
    public List<Question> findAllQuestions() {
        return dsl.selectFrom(TQuestion.T_QUESTION)
                .fetchInto(Question.class);
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
