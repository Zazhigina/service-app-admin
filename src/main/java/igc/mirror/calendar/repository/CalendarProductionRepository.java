package igc.mirror.calendar.repository;

import igc.mirror.calendar.dto.CalendarProductionDto;
import igc.mirror.calendar.model.CalendarProduction;
import igc.mirror.exception.common.EntityNotFoundException;
import igc.mirror.exception.common.EntityNotSavedException;
import jooqdata.tables.TCalendarProduction;
import jooqdata.tables.records.TCalendarProductionRecord;
import org.jooq.DSLContext;
import org.jooq.Record4;
import org.jooq.UpdateConditionStep;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static jooqdata.Keys.T_CALENDAR_PRODUCTION_UN;

@Repository
public class CalendarProductionRepository {
    static final Logger logger = LoggerFactory.getLogger(CalendarProductionRepository.class);

    public static final TCalendarProduction CALENDAR = TCalendarProduction.T_CALENDAR_PRODUCTION;

    @Autowired
    private DSLContext dsl;

    /**
     * Находит все данные производственного календаря
     *
     * @return все данные производственного календаря
     */
    public List<CalendarProductionDto> findCalendarProduction() {
        return dsl.selectFrom(CALENDAR)
                .fetchInto(CalendarProductionDto.class);
    }

    /**
     * Находит данные производственного календаря за указанный год
     *
     * @param year Год
     * @return данные
     */
    public CalendarProductionDto findCalendarProduction(Integer year) {
        return dsl.selectFrom(CALENDAR)
                .where(CALENDAR.YEAR.eq(year))
                .fetchOneInto(CalendarProductionDto.class);
    }

    /**
     * Удаляет запись производственного календаря
     *
     * @param id идентификатор записи
     */
    public void deleteCalendarProduction(Long id) {
        dsl.delete(CALENDAR)
                .where(CALENDAR.ID.eq(id))
                .execute();
    }

    /**
     * Добавляет новые данные в производственный календарь
     *
     * @param calendarProductionItems новые данные
     */
    public void addCalendarProduction(List<CalendarProduction> calendarProductionItems) {
        List<Record4<Integer, BigDecimal, BigDecimal, String>> calendarProductionRecords = new ArrayList<>();

        for (CalendarProduction calendarProductionItem : calendarProductionItems) {
            Record4<Integer, BigDecimal, BigDecimal, String> rec = dsl.newRecord(CALENDAR.YEAR, CALENDAR.HOUR_WORK_COUNT,
                    CALENDAR.MONTH_WORK_HOUR_COUNT, CALENDAR.CREATE_USER);
            rec.set(CALENDAR.YEAR, calendarProductionItem.getYear());
            rec.set(CALENDAR.HOUR_WORK_COUNT, calendarProductionItem.getHourWorkCount());
            rec.set(CALENDAR.MONTH_WORK_HOUR_COUNT, calendarProductionItem.getMonthWorkHourCount());
            rec.set(CALENDAR.CREATE_USER, calendarProductionItem.getCreateUser());
            calendarProductionRecords.add(rec);
        }

        dsl.insertInto(CALENDAR,
                        CALENDAR.YEAR,
                        CALENDAR.HOUR_WORK_COUNT,
                        CALENDAR.MONTH_WORK_HOUR_COUNT,
                        CALENDAR.CREATE_USER)
                .valuesOfRecords(calendarProductionRecords)
                .onConflictOnConstraint(T_CALENDAR_PRODUCTION_UN)
                .doNothing()
                .execute();

        logger.info("Добавлено записей производственного календаря: {}", calendarProductionItems.size());
    }

    /**
     * Обновляет данные производственного календаря
     *
     * @param calendarProductionItems измененные данные
     */
    public void updateCalendarProduction(List<CalendarProduction> calendarProductionItems) {
        List<UpdateConditionStep<TCalendarProductionRecord>> updates = new ArrayList<>();

        for (CalendarProduction calendarProductionItem : calendarProductionItems) {
            updates.add(dsl.update(CALENDAR)
                    .set(CALENDAR.YEAR, calendarProductionItem.getYear())
                    .set(CALENDAR.HOUR_WORK_COUNT, calendarProductionItem.getHourWorkCount())
                    .set(CALENDAR.MONTH_WORK_HOUR_COUNT, calendarProductionItem.getMonthWorkHourCount())
                    .set(CALENDAR.LAST_UPDATE_USER, calendarProductionItem.getLastUpdateUser())
                    .where(CALENDAR.ID.eq(calendarProductionItem.getId())));
        }

        dsl.batch(updates).execute();
        logger.info("Обновлено записей производственного календаря: {}", calendarProductionItems.size());
    }

    /**
     * Добавляет запись в производственный календарь
     *
     * @param calendarProduction новая запись
     */
    public CalendarProduction addCalendarProduction(CalendarProduction calendarProduction) {
        TCalendarProductionRecord record = dsl
                .insertInto(CALENDAR)
                .set(dsl.newRecord(CALENDAR, calendarProduction))
                .returning()
                .fetchOptional()
                .orElseThrow(() -> new EntityNotSavedException(
                        String.format("Возникли ошибки при сохранении записи производственного календаря %s",
                                calendarProduction.getId()), null, CalendarProduction.class));
        return record.into(CalendarProduction.class);
    }


    /**
     * Обновляет запись производственного календаря
     *
     * @param calendarProduction измененная запись
     */
    public CalendarProduction updateCalendarProduction(CalendarProduction calendarProduction) {
        return dsl.update(CALENDAR)
                .set(CALENDAR.YEAR, calendarProduction.getYear())
                .set(CALENDAR.HOUR_WORK_COUNT, calendarProduction.getHourWorkCount())
                .set(CALENDAR.MONTH_WORK_HOUR_COUNT, calendarProduction.getMonthWorkHourCount())
                .set(CALENDAR.LAST_UPDATE_USER, calendarProduction.getLastUpdateUser())
                .where(CALENDAR.ID.eq(calendarProduction.getId()))
                .returningResult(TCalendarProduction.T_CALENDAR_PRODUCTION.fields())
                .fetchOptional()
                .map(r -> r.into(CalendarProduction.class))
                .orElseThrow(() -> new EntityNotFoundException(calendarProduction.getId(), CalendarProduction.class));
    }
}

