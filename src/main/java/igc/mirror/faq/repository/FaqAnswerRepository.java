package igc.mirror.faq.repository;

import igc.mirror.exception.common.EntityNotSavedException;
import igc.mirror.faq.model.FaqAnswer;
import jooqdata.tables.TFaqAnswer;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class FaqAnswerRepository {
    private static TFaqAnswer T_FAQ_ANSWER = TFaqAnswer.T_FAQ_ANSWER;
    @Autowired
    private DSLContext dsl;

    public void save(FaqAnswer data) {
        dsl.insertInto(T_FAQ_ANSWER)
                .set(dsl.newRecord(T_FAQ_ANSWER, data))
                .onDuplicateKeyUpdate()
                .set(dsl.newRecord(T_FAQ_ANSWER, data))
                .returningResult(T_FAQ_ANSWER.ID)
                .fetchOptional()
                .map(r -> r.into(Long.class))
                .orElseThrow(() -> new EntityNotSavedException("Ошибка при сохранении вопроса в БД", data.getId(), FaqAnswer.class));

    }

    public void deleteById(Long id) {
        dsl.deleteFrom(T_FAQ_ANSWER)
                .where(T_FAQ_ANSWER.ID.eq(id))
                .execute();
    }
}
