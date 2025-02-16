package igc.mirror.calendar.repository;

import igc.mirror.auth.UserDetails;
import igc.mirror.calendar.dto.CalendarDayDto;
import igc.mirror.calendar.filter.CalendarDaySearchCriteria;
import igc.mirror.calendar.model.CalendarDay;
import igc.mirror.exception.common.EntityNotFoundException;
import igc.mirror.exception.common.EntityNotSavedException;
import igc.mirror.utils.JooqRepositoryUtil;
import igc.mirror.utils.qfilter.DataFilter;
import igc.mirror.utils.qfilter.QueryBuilder;
import igc.mirror.utils.qfilter.QueryFilter;
import org.jooq.Record;
import org.jooq.*;
import org.jooq.impl.DSL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

import static jooqdata.tables.TCalendarDay.T_CALENDAR_DAY;
import static org.jooq.impl.DSL.month;
import static org.jooq.impl.DSL.year;

@Repository
public class CalendarDayRepository {

    DSLContext dsl;
    JooqRepositoryUtil repositoryUtil;
    UserDetails userDetails;

    @Autowired
    public CalendarDayRepository(DSLContext dsl, JooqRepositoryUtil repositoryUtil, UserDetails userDetails) {
        this.dsl = dsl;
        this.repositoryUtil = repositoryUtil;
        this.userDetails = userDetails;
    }

    public List<CalendarDayDto> findCalendarDaysByFilter(DataFilter<CalendarDaySearchCriteria> dataFilter,
                                                        Pageable pageable) {
        CalendarDaySearchCriteria criteria = (dataFilter != null ? dataFilter.getSearchCriteria() : null);
        QueryFilter subFilter = (dataFilter != null ? dataFilter.getSubFilter() : null);
        return dsl.fetch(QueryBuilder.buildQuery(buildCalendarDaysQuery(criteria), subFilter, pageable))
            .into(CalendarDayDto.class);
    }

    public Long getCalendarDaysCount(DataFilter<CalendarDaySearchCriteria> dataFilter) {
        CalendarDaySearchCriteria criteria = (dataFilter != null ? dataFilter.getSearchCriteria() : null);
        QueryFilter subFilter = (dataFilter != null ? dataFilter.getSubFilter() : null);

        return repositoryUtil.findRecordTotal(QueryBuilder.buildQuery(buildCalendarDaysQuery(criteria), subFilter));
    }

    private Select<? extends Record> buildCalendarDaysQuery(CalendarDaySearchCriteria criteria) {
        Condition condition = DSL.noCondition();

        if (criteria != null) {
            if (criteria.getYear() != null)
                condition = condition.and(year(T_CALENDAR_DAY.DATE).equal(criteria.getYear()));

            if (criteria.getMonth() != null)
                condition = condition.and(month(T_CALENDAR_DAY.DATE).equal(criteria.getMonth()));
        }

        return dsl
            .selectFrom(T_CALENDAR_DAY)
            .where(condition);
    }

    public List<CalendarDayDto> findAllCalendarDays() {
        return dsl
            .selectFrom(T_CALENDAR_DAY)
            .fetchInto(CalendarDayDto.class);
    }

    public CalendarDay getCalendarDay(Long id) {
        return dsl
            .selectFrom(T_CALENDAR_DAY)
            .where(T_CALENDAR_DAY.ID.eq(id))
            .fetchOneInto(CalendarDay.class);
    }

    public CalendarDay createCalendarDay(CalendarDay calendarDay) {
        calendarDay.setCreateUser(userDetails.getUsername());
        return dsl
                .insertInto(T_CALENDAR_DAY)
                .set(dsl.newRecord(T_CALENDAR_DAY, calendarDay))
                .returning()
                .fetchOptionalInto(CalendarDay.class)
                .orElseThrow(() -> new EntityNotSavedException("Ошибка при добавлении записи", null, CalendarDay.class));
    }

    public CalendarDay changeCalendarDay(CalendarDay calendarDay) {
        return dsl.update(T_CALENDAR_DAY)
                .set(T_CALENDAR_DAY.DATE, calendarDay.getDate())
                .set(T_CALENDAR_DAY.WORKING, calendarDay.getWorking())
                .set(T_CALENDAR_DAY.LAST_UPDATE_USER, userDetails.getUsername())
                .where(T_CALENDAR_DAY.ID.eq(calendarDay.getId()))
                .returning()
                .fetchOptionalInto(CalendarDay.class)
                .orElseThrow(() -> new EntityNotSavedException("Запись не найдена", calendarDay.getId(), CalendarDay.class));
    }

    public void deleteCalendarDay(Long id) {
        dsl.delete(T_CALENDAR_DAY)
            .where(T_CALENDAR_DAY.ID.eq(id))
            .execute();
    }
}

