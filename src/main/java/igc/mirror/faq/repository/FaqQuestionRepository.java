package igc.mirror.faq.repository;

import igc.mirror.exception.common.EntityNotFoundException;
import igc.mirror.exception.common.EntityNotSavedException;
import igc.mirror.faq.filter.FaqQuestionSearchCriteria;
import igc.mirror.faq.model.FaqQuestion;
import igc.mirror.utils.qfilter.DataFilter;
import igc.mirror.utils.qfilter.QueryBuilder;
import jooqdata.tables.TFaqAnswer;
import jooqdata.tables.TFaqQuestion;
import org.jooq.*;
import org.jooq.Record;
import org.jooq.impl.DSL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;

import static org.jooq.impl.DSL.*;

@Repository
public class FaqQuestionRepository {
    private static TFaqQuestion T_FAQ_QUESTION = TFaqQuestion.T_FAQ_QUESTION;
    @Autowired
    private DSLContext dsl;

    public FaqQuestion findById(Long id) {
        return dsl.fetchOptional(buildDefaultQuery()
                        .where(T_FAQ_QUESTION.ID.eq(id)))
                .orElseThrow(() -> new EntityNotFoundException(id, FaqQuestion.class))
                .into(FaqQuestion.class);

    }

    public Page<FaqQuestion> findByFilters(DataFilter<FaqQuestionSearchCriteria> dataFilter, Pageable pageable) {
        Objects.requireNonNull(dataFilter);

        List<FaqQuestion> faqQuestions = dsl.fetch(QueryBuilder.buildQuery(buildQueryWithFilter(dataFilter.getSearchCriteria()), dataFilter.getSubFilter(), pageable))
                .into(FaqQuestion.class);

        long total = faqQuestions.size();

        if (total >= pageable.getPageSize()) {
            total = dsl.fetchCount(QueryBuilder.buildQuery(buildQueryWithFilter(dataFilter.getSearchCriteria()), dataFilter.getSubFilter()));
        }

        return new PageImpl<>(faqQuestions, pageable, total);

    }

    public FaqQuestion save(FaqQuestion data) {
        Long savedId = dsl.insertInto(T_FAQ_QUESTION)
                .set(dsl.newRecord(T_FAQ_QUESTION, data))
                .onDuplicateKeyUpdate()
                .set(dsl.newRecord(T_FAQ_QUESTION, data))
                .returningResult(T_FAQ_QUESTION.ID)
                .fetchOptional()
                .map(r -> r.into(Long.class))
                .orElseThrow(() -> new EntityNotSavedException("Ошибка при сохранении вопроса в БД", data.getId(), FaqQuestion.class));

        return findById(savedId);

    }

    public void deleteById(Long id) {
        dsl.deleteFrom(T_FAQ_QUESTION)
                .where(T_FAQ_QUESTION.ID.eq(id))
                .execute();
    }

    private Select<? extends Record> buildQueryWithFilter(FaqQuestionSearchCriteria criteria) {
        Objects.requireNonNull(criteria);

        Condition condition = DSL.noCondition();

        if (StringUtils.hasText(criteria.getName()))
            condition = condition.and(T_FAQ_QUESTION.NAME.likeIgnoreCase(criteria.getLikePattern(criteria.getName())));

        if (criteria.getMirrorService() != null)
            condition = condition.and(T_FAQ_QUESTION.SERVICE_NAME.eq(criteria.getMirrorService().name()));

        if (criteria.getQuestionType() != null)
            condition = condition.and(T_FAQ_QUESTION.QUESTION_TYPE.eq(criteria.getQuestionType().name()));

        return
                buildDefaultQuery()
                        .where(condition);
    }

    private SelectJoinStep<Record> buildDefaultQuery() {
        return
                select(T_FAQ_QUESTION.asterisk(),
                        field(
                                select(row(TFaqAnswer.T_FAQ_ANSWER.fields()))
                                        .from(TFaqAnswer.T_FAQ_ANSWER)
                                        .where(TFaqAnswer.T_FAQ_ANSWER.QUESTION_ID.eq(T_FAQ_QUESTION.ID))
                                        .limit(1)
                        ).as("answer"))
                        .from(T_FAQ_QUESTION);
    }

}
