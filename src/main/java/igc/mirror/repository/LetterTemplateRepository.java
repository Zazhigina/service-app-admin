package igc.mirror.repository;

import igc.mirror.exception.common.EntityNotFoundException;
import igc.mirror.filter.LetterTemplateSearchCriteria;
import igc.mirror.model.LetterTemplate;
import igc.mirror.utils.jooq.JooqCommonRepository;
import igc.mirror.utils.qfilter.DataFilter;
import igc.mirror.utils.qfilter.QueryBuilder;
import jooqdata.tables.TLetterTemplate;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Select;
import org.jooq.impl.DSL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

import static org.jooq.impl.DSL.select;

@Repository
public class LetterTemplateRepository implements JooqCommonRepository<LetterTemplate, Long> {
    private final DSLContext dsl;

    @Autowired
    public LetterTemplateRepository(DSLContext dsl) {
        this.dsl = dsl;
    }

    @Override
    public LetterTemplate find(Long identifier) {
        return
                dsl
                    .select()
                    .from(TLetterTemplate.T_LETTER_TEMPLATE)
                    .where(TLetterTemplate.T_LETTER_TEMPLATE.ID.eq(identifier))
                    .fetchOptional()
                    .orElseThrow(() -> new EntityNotFoundException(identifier, LetterTemplate.class))
                    .into(LetterTemplate.class);
    }

    @Override
    public Page<LetterTemplate> findByFilters(DataFilter<?> dataFilter, Pageable pageable) {
        LetterTemplateSearchCriteria criteria = (dataFilter != null ? (LetterTemplateSearchCriteria) dataFilter.getSearchCriteria() : null);

        List<LetterTemplate> letterTemplateList = dsl.fetch(QueryBuilder.buildQuery(initLetterTemplateQuery(criteria), dataFilter.getSubFilter(), pageable))
                .into(LetterTemplate.class);

        long total = letterTemplateList.size();

        if(total >= pageable.getPageSize()){
            total = dsl.fetchCount(QueryBuilder.buildQuery(initLetterTemplateQuery(criteria), dataFilter.getSubFilter()));
        }

        return new PageImpl<>(letterTemplateList, pageable, total);
    }

    @Override
    public List<LetterTemplate> findByFilters(DataFilter<?> dataFilter) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Boolean checkExist(Long identifier) {
        throw new UnsupportedOperationException();
    }

    @Override
    public LetterTemplate save(LetterTemplate letterTemplate) {
        return
                dsl
                    .insertInto(TLetterTemplate.T_LETTER_TEMPLATE)
                        .set(dsl.newRecord(TLetterTemplate.T_LETTER_TEMPLATE, letterTemplate))
                    .onDuplicateKeyUpdate()
                        .set(dsl.newRecord(TLetterTemplate.T_LETTER_TEMPLATE, letterTemplate))
                    .returningResult(TLetterTemplate.T_LETTER_TEMPLATE)
                    .fetchOptional()
                    .orElseThrow(() -> new EntityNotFoundException(letterTemplate.getId(), LetterTemplate.class))
                    .into(LetterTemplate.class);
    }

    @Override
    public List<LetterTemplate> saveList(List<LetterTemplate> dataList) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void delete(Long identifier) {
        dsl
            .deleteFrom(TLetterTemplate.T_LETTER_TEMPLATE)
            .where(TLetterTemplate.T_LETTER_TEMPLATE.ID.eq(identifier))
            .execute();
    }

    @Override
    public void deleteList(List<Long> identifierList) {
        throw new UnsupportedOperationException();
    }

    /**
     * Поиск шаблона по наименованию параметра
     * @param letterType наименование параметра
     * @return Данные шаблона
     */
    public LetterTemplate findByLetterType(String letterType){
        return
            dsl
            .select()
            .from(TLetterTemplate.T_LETTER_TEMPLATE)
            .where(TLetterTemplate.T_LETTER_TEMPLATE.LETTER_TYPE.eq(letterType))
            .fetchOptional()
            .orElseThrow(() -> new EntityNotFoundException(null, LetterTemplate.class))
            .into(LetterTemplate.class);
    }

    private Select<? extends Record> initLetterTemplateQuery(LetterTemplateSearchCriteria criteria){
        Condition condition = DSL.noCondition();

        if(criteria != null){
            if(criteria.getLetterType() != null){
                condition = condition.and(TLetterTemplate.T_LETTER_TEMPLATE.LETTER_TYPE.likeIgnoreCase(criteria.getLikePattern(criteria.getLetterType())));
            }

            if(criteria.getTitle() != null){
                condition = condition.and(TLetterTemplate.T_LETTER_TEMPLATE.TITLE.likeIgnoreCase(criteria.getLikePattern(criteria.getTitle())));
            }
        }

        return
            select(TLetterTemplate.T_LETTER_TEMPLATE.fields())
            .from(TLetterTemplate.T_LETTER_TEMPLATE)
            .where(condition);
    }
}
