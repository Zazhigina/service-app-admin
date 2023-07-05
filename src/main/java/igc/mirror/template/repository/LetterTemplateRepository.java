package igc.mirror.template.repository;

import igc.mirror.template.dto.LetterTemplateBriefInfoDto;
import igc.mirror.exception.common.EntityNotFoundException;
import igc.mirror.template.filter.LetterTemplateSearchCriteria;
import igc.mirror.template.model.LetterTemplate;
import igc.mirror.template.ref.LetterTemplateType;
import igc.mirror.utils.JooqRepositoryUtil;
import igc.mirror.utils.jooq.JooqCommonRepository;
import igc.mirror.utils.qfilter.DataFilter;
import igc.mirror.utils.qfilter.QueryBuilder;
import igc.mirror.utils.qfilter.QueryFilter;
import jakarta.annotation.PostConstruct;
import jooqdata.tables.TLetterTemplate;
import jooqdata.tables.TLetterTemplateTypeTemplateEnum;
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
    private final JooqRepositoryUtil jooqRepositoryUtil;

    @Autowired
    public LetterTemplateRepository(DSLContext dsl, JooqRepositoryUtil jooqRepositoryUtil) {
        this.dsl = dsl;
        this.jooqRepositoryUtil = jooqRepositoryUtil;
    }

    /**
     * Заполняет настроечные данные для enum Тип шаблона
     */
    @PostConstruct
    private void fillLetterTemplateTypes() {
        dsl.selectFrom(TLetterTemplateTypeTemplateEnum.T_LETTER_TEMPLATE_TYPE_TEMPLATE_ENUM)
                .fetch()
                .forEach(r -> LetterTemplateType.fill(r.getName(), r.getDescription()));
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
    @Deprecated
    public Page<LetterTemplate> findByFilters(DataFilter<?> dataFilter, Pageable pageable) {
        LetterTemplateSearchCriteria criteria = (dataFilter != null ? (LetterTemplateSearchCriteria) dataFilter.getSearchCriteria() : null);

        List<LetterTemplate> letterTemplateList = dsl.fetch(QueryBuilder.buildQuery(initLetterTemplateQuery(criteria), dataFilter.getSubFilter(), pageable))
                .into(LetterTemplate.class);

        long total = letterTemplateList.size();

        if (total >= pageable.getPageSize()) {
            total = dsl.fetchCount(QueryBuilder.buildQuery(initLetterTemplateQuery(criteria), dataFilter.getSubFilter()));
        }

        return new PageImpl<>(letterTemplateList, pageable, total);
    }

    public List<LetterTemplateBriefInfoDto> findTemplates(DataFilter<LetterTemplateSearchCriteria> dataFilter, Pageable pageable) {
        LetterTemplateSearchCriteria criteria = (dataFilter != null ? dataFilter.getSearchCriteria() : null);
        QueryFilter subFilter = (dataFilter != null ? dataFilter.getSubFilter() : null);

        return dsl.fetch(QueryBuilder.buildQuery(initLetterTemplateQuery(criteria), subFilter, pageable))
                .into(LetterTemplateBriefInfoDto.class);
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
     *
     * @param letterType наименование параметра
     * @return Данные шаблона
     */
    public LetterTemplate findByLetterType(String letterType) {
        return
                dsl
                        .select()
                        .from(TLetterTemplate.T_LETTER_TEMPLATE)
                        .where(TLetterTemplate.T_LETTER_TEMPLATE.LETTER_TYPE.eq(letterType))
                        .fetchOptional()
                        .orElseThrow(() -> new EntityNotFoundException(null, LetterTemplate.class))
                        .into(LetterTemplate.class);
    }

    private Select<? extends Record> initLetterTemplateQuery(LetterTemplateSearchCriteria criteria) {
        Condition condition = DSL.noCondition();

        if (criteria != null) {
            if (criteria.getLetterType() != null) {
                condition = condition.and(TLetterTemplate.T_LETTER_TEMPLATE.LETTER_TYPE.likeIgnoreCase(criteria.getLikePattern(criteria.getLetterType())));
            }

            if (criteria.getTitle() != null) {
                condition = condition.and(TLetterTemplate.T_LETTER_TEMPLATE.TITLE.likeIgnoreCase(criteria.getLikePattern(criteria.getTitle())));
            }
        }

        return
                select(TLetterTemplate.T_LETTER_TEMPLATE.ID,
                        TLetterTemplate.T_LETTER_TEMPLATE.LETTER_TYPE,
                        TLetterTemplate.T_LETTER_TEMPLATE.TITLE,
                        TLetterTemplate.T_LETTER_TEMPLATE.TYPE_TEMPLATE,
                        TLetterTemplate.T_LETTER_TEMPLATE.LETTER_SAMPLE,
                        TLetterTemplateTypeTemplateEnum.T_LETTER_TEMPLATE_TYPE_TEMPLATE_ENUM.DESCRIPTION.as("typeTemplateName"))
                        .from(TLetterTemplate.T_LETTER_TEMPLATE)
                        .leftJoin(TLetterTemplateTypeTemplateEnum.T_LETTER_TEMPLATE_TYPE_TEMPLATE_ENUM).on(TLetterTemplateTypeTemplateEnum.T_LETTER_TEMPLATE_TYPE_TEMPLATE_ENUM.NAME.equal(TLetterTemplate.T_LETTER_TEMPLATE.TYPE_TEMPLATE))
                        .where(condition);
    }

    public Long getTemplatesCount(DataFilter<LetterTemplateSearchCriteria> dataFilter) {
        LetterTemplateSearchCriteria criteria = (dataFilter != null ? dataFilter.getSearchCriteria() : null);
        QueryFilter subFilter = (dataFilter != null ? dataFilter.getSubFilter() : null);

        return jooqRepositoryUtil.findRecordTotal(QueryBuilder.buildQuery(initLetterTemplateQuery(criteria), subFilter));
    }
}
