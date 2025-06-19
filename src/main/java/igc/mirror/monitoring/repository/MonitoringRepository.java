package igc.mirror.monitoring.repository;

import igc.mirror.exception.common.EntityDuplicatedException;
import igc.mirror.exception.common.EntityNotFoundException;
import igc.mirror.exception.common.EntityNotSavedException;
import igc.mirror.monitoring.dto.MonitoringDataSaveDto;
import igc.mirror.monitoring.model.MonitoringData;
import igc.mirror.monitoring.model.MonitoringStatistic;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.Table;
import org.jooq.impl.DSL;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.*;

import static jooqdata.tables.TMonitoring.T_MONITORING;
import static jooqdata.tables.TMonitoringStatistics.T_MONITORING_STATISTICS;
import static org.jooq.impl.DSL.*;

@Repository
@RequiredArgsConstructor
@Slf4j
public class MonitoringRepository {

    private final DSLContext dsl;

    /**
     * Добавляет запись в мониторинг
     *
     * @param data новая запись
     * @return сохраненная в базе данных новая запись
     */
    public MonitoringData add(MonitoringDataSaveDto data) {
        if (checkExist(data)) {
            throw new EntityDuplicatedException(
                    String.format("Ошибка: запись с URL %s уже существует!", data.getUrl()),
                    null, MonitoringData.class
            );
        }
        return save(data);
    }

    /**
     * Проверка существования записи
     *
     * @param data запись
     */
    private Boolean checkExist(MonitoringDataSaveDto data) {
        return dsl
                .fetchExists(selectOne()
                        .from(T_MONITORING)
                        .where(T_MONITORING.SERVICE_NAME.eq(data.getServiceName())
                                .and(T_MONITORING.URL.eq(data.getUrl())))
                );
    }


    /**
     * Сохранение данных мониторинга
     *
     * @param data новая запись
     * @return сохраненная в базе данных новая запись
     */
    private MonitoringData save(MonitoringDataSaveDto data) {
        MonitoringData savedData = dsl.insertInto(T_MONITORING)
                .set(T_MONITORING.SERVICE_NAME, data.getServiceName())
                .set(T_MONITORING.URL, data.getUrl())
                .set(T_MONITORING.SUMMARY, data.getSummary())
                .returningResult(T_MONITORING.ID, T_MONITORING.SERVICE_NAME, T_MONITORING.URL, T_MONITORING.SUMMARY, T_MONITORING.IS_ACTIVE)
                .fetchOptional()
                .orElseThrow(() ->
                        new EntityNotSavedException("Ошибка при сохранении данных мониторинга в БД", null, MonitoringData.class))
                .into(MonitoringData.class);

        return savedData;
    }

    /**
     * Обновляет данные мониторинга сервисов
     *
     * @param updateFields новые данные
     * @param id           данные, которые нужно обновить
     * @return обновленные данные в записи мониторинга сервисов
     */
    public MonitoringData updateInDb(Map<Field<?>, Object> updateFields, Long id) {
        MonitoringData updateData = dsl.update(T_MONITORING)
                .set(updateFields)
                .where(T_MONITORING.ID.eq(id))
                .returningResult(T_MONITORING.ID, T_MONITORING.SERVICE_NAME, T_MONITORING.URL, T_MONITORING.SUMMARY, T_MONITORING.IS_ACTIVE)
                .fetchOptional()
                .orElseThrow(() -> new EntityNotSavedException("Ошибка при обновлении данных мониторинга в БД", null, MonitoringDataSaveDto.class))
                .into(MonitoringData.class);

        return updateData;
    }


    /**
     * Получает запись мониторинга сервисов по ID
     *
     * @param id данные, которые нужно получить
     * @return запись MonitoringData
     */
    public MonitoringData findMonitoringById(Long id) {

        return dsl.select(
                        T_MONITORING.ID,
                        T_MONITORING.SERVICE_NAME,
                        T_MONITORING.URL,
                        T_MONITORING.SUMMARY,
                        T_MONITORING.IS_ACTIVE
                )
                .from(T_MONITORING)
                .where(T_MONITORING.ID.eq(id))
                .fetchOptional()
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format("Ошибка при поиске записи для мониторинга в БД. Запись с id = %s не существует ", id),
                        id, MonitoringData.class
                ))
                .into(MonitoringData.class);
    }


    /**
     * Получает все записи мониторинга сервисов
     *
     * @return список всех записей MonitoringData
     */
    public List<MonitoringData> findAllMonitoringData() {
        return dsl.select(
                        T_MONITORING.ID,
                        T_MONITORING.SERVICE_NAME,
                        T_MONITORING.URL,
                        T_MONITORING.SUMMARY,
                        T_MONITORING.IS_ACTIVE)
                .from(T_MONITORING)
                .fetchInto(MonitoringData.class);
    }


    /**
     * Получает все актуальные записи статистики мониторинга сервисов
     *
     * @return список актуальных записей из MonitoringWithStatsDto
     */
    public List<MonitoringStatistic> findAllActualMonitoring() {
        Table<?> latestStats = DSL
                .select(
                        T_MONITORING_STATISTICS.URL,
                        DSL.max(T_MONITORING_STATISTICS.CREATE_DATE).as("max_date")
                )
                .from(T_MONITORING_STATISTICS)
                .where(T_MONITORING_STATISTICS.DELETED.eq(false))
                .groupBy(T_MONITORING_STATISTICS.URL)
                .asTable("latest_stats");

        return dsl.select(
                        T_MONITORING.SERVICE_NAME,
                        T_MONITORING.URL,
                        T_MONITORING.SUMMARY,
                        T_MONITORING_STATISTICS.RESULT_CHECK,
                        T_MONITORING_STATISTICS.CREATE_DATE
                )
                .from(T_MONITORING)
                .join(latestStats)
                .on(T_MONITORING.URL.eq(latestStats.field("url", String.class)))
                .join(T_MONITORING_STATISTICS)
                .on(
                        T_MONITORING_STATISTICS.URL.eq(latestStats.field("url", String.class))
                                .and(T_MONITORING_STATISTICS.CREATE_DATE.eq(latestStats.field("max_date", LocalDateTime.class)))
                )
                .where(T_MONITORING.IS_ACTIVE.eq(true))
                .fetchInto(MonitoringStatistic.class);


    }

    /**
     * Получает все записи статистики мониторинга сервисов
     *
     * @return список актуальных записей из MonitoringWithStatsDto
     */

    public List<MonitoringStatistic> findAllMonitoring() {
        return dsl.select(
                        T_MONITORING_STATISTICS.ID,
                        T_MONITORING_STATISTICS.SERVICE_NAME,
                        T_MONITORING_STATISTICS.URL/*.as("monitoringData.url")*/,
                        T_MONITORING_STATISTICS.SUMMARY,
                        T_MONITORING_STATISTICS.RESULT_CHECK,
                        T_MONITORING_STATISTICS.CREATE_DATE,
                        T_MONITORING_STATISTICS.DELETED
                )
                .from(T_MONITORING_STATISTICS)
                .fetchInto(MonitoringStatistic.class);
    }

    /**
     * Удаляет запись мониторинга сервисов
     *
     * @param id данные, которые нужно удалить
     */
    public void deleteMonitoringById(Long id) {
        findMonitoringById(id);

        dsl.delete(T_MONITORING)
                .where(T_MONITORING.ID.eq(id))
                .execute();
    }


    /**
     * Получение всех URL, которые активны
     *
     * @return список всех URL
     */
    public List<String> getAllMonitoringUrls() {
        return dsl.select(T_MONITORING.URL)
                .from(T_MONITORING)
                .where(T_MONITORING.IS_ACTIVE)
                .fetchInto(String.class);
    }

    /**
     * Обновление статуса и вставляем запись в t_monitoring_statistics
     *
     * @param url    путь по которому была проверка
     * @param status статус после проверки
     */
    public void updateStatus(String url, String status) {
        MonitoringData monitoring = dsl.select(
                        T_MONITORING.ID,
                        T_MONITORING.SERVICE_NAME,
                        T_MONITORING.URL,
                        T_MONITORING.SUMMARY,
                        T_MONITORING.IS_ACTIVE
                )
                .from(T_MONITORING)
                .where(T_MONITORING.URL.eq(url))
                .fetchOneInto(MonitoringData.class);

        if (monitoring == null) {
            throw new EntityNotFoundException(
                    String.format("Ошибка обновления: запись с URL '%s' не найдена!", url),
                    null, MonitoringData.class);
        }

        // Вставляем запись в t_monitoring_statistics
        dsl.insertInto(T_MONITORING_STATISTICS)
                .set(T_MONITORING_STATISTICS.SERVICE_NAME, monitoring.getServiceName())
                .set(T_MONITORING_STATISTICS.URL, monitoring.getUrl())
                .set(T_MONITORING_STATISTICS.SUMMARY, monitoring.getSummary())
                .set(T_MONITORING_STATISTICS.RESULT_CHECK, status)
                .set(T_MONITORING_STATISTICS.CREATE_DATE, LocalDateTime.now())
                .set(T_MONITORING_STATISTICS.DELETED, false)
                .execute();
    }

    public void deactivateStatsByIdMonitoring(Long id) {
        List<MonitoringData> monitoring = dsl.select(T_MONITORING.URL, T_MONITORING.SERVICE_NAME)
                .from(T_MONITORING)
                .where(T_MONITORING.ID.eq(id))
                .fetchInto(MonitoringData.class);

        monitoring.forEach(m ->
                dsl.update(T_MONITORING_STATISTICS)
                        .set(T_MONITORING_STATISTICS.DELETED, true)
                        .where(T_MONITORING_STATISTICS.URL.eq(m.getUrl()).and(T_MONITORING_STATISTICS.SERVICE_NAME.eq(m.getServiceName())))
                        .and(T_MONITORING_STATISTICS.DELETED.eq(false))
                        .execute());
    }


    public void deleteMonitoringStatistic() {
        dsl.delete(T_MONITORING_STATISTICS).execute();
    }
}