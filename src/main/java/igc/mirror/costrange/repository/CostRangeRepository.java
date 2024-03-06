package igc.mirror.costrange.repository;

import igc.mirror.costrange.dto.CostRangeDto;
import igc.mirror.costrange.filter.CostRangeSearchCriteria;
import igc.mirror.costrange.model.CostRange;
import igc.mirror.utils.qfilter.QueryBuilder;
import igc.mirror.utils.qfilter.QueryFilter;
import jooqdata.tables.TCostRange;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Select;
import org.jooq.impl.DSL;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

import static org.jooq.impl.DSL.select;

@Repository
public class CostRangeRepository {

    private static final TCostRange T_COST_RANGE = TCostRange.T_COST_RANGE;

    private final DSLContext dsl;

    public CostRangeRepository(DSLContext dsl) {
        this.dsl = dsl;
    }

    public Page<CostRange> findCostRangeByFilters(CostRangeSearchCriteria criteria, Pageable pageable) {

        List<CostRange> costRanges = dsl.fetch(QueryBuilder.buildQuery(initCostRangeFilterQuery(criteria), null, pageable))
                .into(CostRange.class);

        long total = costRanges.size();

        if (total >= pageable.getPageSize()) {
            total = dsl.fetchCount(QueryBuilder.buildQuery(initCostRangeFilterQuery(criteria), (QueryFilter) null));
        }

        return new PageImpl<>(costRanges, pageable, total);
    }

    private Select<? extends Record> initCostRangeFilterQuery(CostRangeSearchCriteria criteria) {

        Condition condition = DSL.noCondition();

        if (criteria != null) {
            if (criteria.getLowerBound() != null) {
                condition = condition.and(
                        T_COST_RANGE.LOWER_BOUND.likeIgnoreCase(criteria.getLikePattern(criteria.getLowerBound()))
                );
            }

            if (criteria.getUpperBound() != null) {
                condition = condition.and(
                        T_COST_RANGE.UPPER_BOUND.likeIgnoreCase(criteria.getLikePattern(criteria.getUpperBound()))
                );
            }

            if (criteria.getIntervalStep() != null) {
                condition = condition.and(
                        T_COST_RANGE.INTERVAL_STEP.likeIgnoreCase(criteria.getLikePattern(criteria.getIntervalStep()))
                );
            }

            if (criteria.getRangeText() != null) {
                condition = condition.and(
                        T_COST_RANGE.RANGE_TEXT.likeIgnoreCase(criteria.getLikePattern(criteria.getRangeText()))
                );
            }
        }

        return   select(T_COST_RANGE.ID,
                        T_COST_RANGE.LOWER_BOUND,
                        T_COST_RANGE.UPPER_BOUND,
                        T_COST_RANGE.INTERVAL_STEP,
                        T_COST_RANGE.RANGE_TEXT,
                        T_COST_RANGE.CREATE_DATE,
                        T_COST_RANGE.CREATE_USER,
                        T_COST_RANGE.LAST_UPDATE_DATE,
                        T_COST_RANGE.LAST_UPDATE_USER)
                .from(T_COST_RANGE)
                .where(condition);
    }

    public CostRange getCostRangeById(Long id) {
        return dsl.select(T_COST_RANGE.fields())
                .from(T_COST_RANGE)
                .where(T_COST_RANGE.ID.equal(id))
                .fetchAnyInto(CostRange.class);
    }

    public Long updateCostRangeById(CostRange сostRange) {

        dsl.update(T_COST_RANGE)
                .set(T_COST_RANGE.LOWER_BOUND, сostRange.getLowerBound())
                .set(T_COST_RANGE.UPPER_BOUND, сostRange.getUpperBound())
                .set(T_COST_RANGE.INTERVAL_STEP, сostRange.getIntervalStep())
                .set(T_COST_RANGE.RANGE_TEXT, сostRange.getRangeText())                
                .set(T_COST_RANGE.LAST_UPDATE_USER, сostRange.getLastUpdateUser())
                .where(T_COST_RANGE.ID.equal(сostRange.getId()))
                .execute();

        return сostRange.getId();
    }

    public CostRange getCostRangeByKey(CostRangeDto newCostRange) {

        return dsl.select(T_COST_RANGE.fields())
                .from(T_COST_RANGE)
                .where(T_COST_RANGE.LOWER_BOUND.equal(newCostRange.getLowerBound()))
                .and(T_COST_RANGE.UPPER_BOUND.equal(newCostRange.getUpperBound()))
                .fetchAnyInto(CostRange.class);
    }

    public CostRangeDto insertServiceVersion(CostRange сostRange) {

        var newRecord = dsl.insertInto(T_COST_RANGE,
                        T_COST_RANGE.LOWER_BOUND,
                        T_COST_RANGE.UPPER_BOUND,
                        T_COST_RANGE.INTERVAL_STEP,
                        T_COST_RANGE.RANGE_TEXT,
                        T_COST_RANGE.CREATE_USER
                )
                .values(сostRange.getLowerBound(),
                        сostRange.getUpperBound(),
                        сostRange.getIntervalStep(),
                        сostRange.getRangeText(),
                        сostRange.getCreateUser()
                )
                .returning()
                .fetchOne();

        сostRange.setId(newRecord.getId());
        сostRange.setCreateDate(newRecord.getCreateDate());

        return new CostRangeDto(сostRange);
    }

    public Long deleteServiceVersionById(Long id) {
        return dsl.deleteFrom(T_COST_RANGE)
                .where(T_COST_RANGE.ID.equal(id))
                .returningResult(T_COST_RANGE.ID)
                .fetchOneInto(Long.class);
    }

    public List<CostRangeDto> getCostRanges() {
        return dsl.select(T_COST_RANGE.fields())
                .from(T_COST_RANGE)
                .fetchInto(CostRangeDto.class);
    }
}
