package igc.mirror.monitoring.repository;

import igc.mirror.exception.common.EntityDuplicatedException;
import igc.mirror.exception.common.EntityNotFoundException;
import igc.mirror.exception.common.EntityNotSavedException;
import igc.mirror.monitoring.dto.MonitoringDataSaveDto;
import igc.mirror.monitoring.model.MonitoringData;
import igc.mirror.monitoring.model.MonitoringStatistic;
import igc.mirror.monitoring.model.ServiceData;
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
import static jooqdata.tables.TServiceDate.T_SERVICE_DATE;
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
    public MonitoringData add(MonitoringData data) {
        if (checkExist(data)) {
            throw new EntityDuplicatedException(
                    String.format("Ошибка: запись с URL %s уже существует!", data.getUrl()),
                    data.getId(), MonitoringData.class
            );
        }
        return save(data);
    }

    /**
     * Проверка существования записи
     *
     * @param data запись
     */
    private Boolean checkExist(MonitoringData data) {
        return dsl
                .fetchExists(selectOne()
                        .from(T_MONITORING)
                        .join(T_SERVICE_DATE)
                        .on(T_MONITORING.SERVICE_DATE_ID.eq(T_SERVICE_DATE.ID))
                        .where(T_SERVICE_DATE.NAME.eq(data.getServiceData().getName()))
                );
    }

    /**
     * Получение сервиса по имени
     *
     * @param name имя сервиса
     */
    public ServiceData findServiceByName(String name) {
        return dsl.selectFrom(T_SERVICE_DATE)
                .where(T_SERVICE_DATE.NAME.eq(name))
                .fetchOptional()
                .orElseThrow(() -> new EntityNotFoundException(
                        "Сервис с именем " + name + " не найден", null, ServiceData.class))
                .into(ServiceData.class);
    }


    /**
     * Сохранение данных мониторинга
     *
     * @param data новая запись
     * @return сохраненная в базе данных новая запись
     */
    private MonitoringData save(MonitoringData data) {
        MonitoringData savedData = dsl.insertInto(T_MONITORING)
                .set(T_MONITORING.SERVICE_DATE_ID, data.getServiceData().getId())
                .set(T_MONITORING.URL, data.getUrl())
                .set(T_MONITORING.SUMMARY, data.getSummary())
                .returningResult(T_MONITORING.ID, T_MONITORING.URL, T_MONITORING.SUMMARY)
                .fetchOptional()
                .orElseThrow(() ->
                        new EntityNotSavedException("Ошибка при сохранении данных мониторинга в БД", null, MonitoringData.class))
                .into(MonitoringData.class);

        ServiceData serviceData = dsl.selectFrom(T_SERVICE_DATE)
                .where(T_SERVICE_DATE.ID.eq(data.getServiceData().getId()))
                .fetchOneInto(ServiceData.class);

        savedData.setServiceData(serviceData);

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
                .returningResult(T_MONITORING.ID, T_MONITORING.SERVICE_DATE_ID, T_MONITORING.URL, T_MONITORING.SUMMARY, T_MONITORING.IS_ACTIVE)
                .fetchOptional()
                .orElseThrow(() -> new EntityNotSavedException("Ошибка при обновлении данных мониторинга в БД", null, MonitoringDataSaveDto.class))
                .into(MonitoringData.class);

        ServiceData serviceData = dsl.select(T_SERVICE_DATE)
                .from(T_MONITORING)
                .join(T_SERVICE_DATE).on(T_MONITORING.SERVICE_DATE_ID.eq(T_SERVICE_DATE.ID))
                .where(T_MONITORING.ID.eq(id))
                .fetchOneInto(ServiceData.class);

        updateData.setServiceData(serviceData);

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
                        T_SERVICE_DATE.ID.as("serviceData.id"),
                        T_SERVICE_DATE.NAME.as("serviceData.name"),
                        T_SERVICE_DATE.DESCRIPTION.as("serviceData.description"),
                        T_MONITORING.ID,
                        T_MONITORING.URL,
                        T_MONITORING.SUMMARY,
                        T_MONITORING.IS_ACTIVE
                )
                .from(T_MONITORING)
                .join(T_SERVICE_DATE).on(T_MONITORING.SERVICE_DATE_ID.eq(T_SERVICE_DATE.ID))
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
                        T_SERVICE_DATE.NAME.as("serviceData.name"),
                        T_SERVICE_DATE.DESCRIPTION.as("serviceData.description"),
                        T_MONITORING.URL,
                        T_MONITORING.SUMMARY,
                        T_MONITORING.IS_ACTIVE)
                .from(T_MONITORING)
                .join(T_SERVICE_DATE).on(T_MONITORING.SERVICE_DATE_ID.eq(T_SERVICE_DATE.ID))
                .fetchInto(MonitoringData.class);
    }


    /**
     * Получает все актуальные записи статистики мониторинга сервисов
     *
     * @return список актуальных записей из MonitoringWithStatsDto
     */
    public List<MonitoringStatistic> findAllActualMonitoring() {
        Table<?> latestStats = DSL.select(
                        T_MONITORING_STATISTICS.MONITORING_ID,
                        DSL.max(T_MONITORING_STATISTICS.CREATE_DATE).as("max_date")
                )
                .from(T_MONITORING_STATISTICS)
                .where(T_MONITORING_STATISTICS.DELETED.eq(false))
                .groupBy(T_MONITORING_STATISTICS.MONITORING_ID)
                .asTable("latest_stats");

        return dsl.select(
                        T_SERVICE_DATE.NAME.as("monitoringData.serviceData.name"),
                        T_SERVICE_DATE.DESCRIPTION.as("monitoringData.serviceData.description"),
                        T_MONITORING.URL.as("monitoringData.url"),
                        T_MONITORING.SUMMARY.as("monitoringData.summary"),
                        T_MONITORING_STATISTICS.RESULT_CHECK,
                        T_MONITORING_STATISTICS.CREATE_DATE
                )
                .from(T_MONITORING_STATISTICS)
                .join(latestStats)
                .on(T_MONITORING_STATISTICS.MONITORING_ID.eq(latestStats.field("monitoring_id", Long.class))
                        .and(T_MONITORING_STATISTICS.CREATE_DATE.eq(latestStats.field("max_date", LocalDateTime.class))))
                .join(T_MONITORING).on(T_MONITORING_STATISTICS.MONITORING_ID.eq(T_MONITORING.ID))
                .join(T_SERVICE_DATE).on(T_MONITORING.SERVICE_DATE_ID.eq(T_SERVICE_DATE.ID))
                .fetchInto(MonitoringStatistic.class);
    }

    /**
     * Получает все записи статистики мониторинга сервисов
     *
     * @return список актуальных записей из MonitoringWithStatsDto
     */

    public List<MonitoringStatistic> findAllMonitoring() {
        return dsl.select(
                        T_SERVICE_DATE.NAME.as("monitoringData.serviceData.name"),
                        T_SERVICE_DATE.DESCRIPTION.as("monitoringData.serviceData.description"),
                        T_MONITORING.URL.as("monitoringData.url"),
                        T_MONITORING.SUMMARY.as("monitoringData.summary"),
                        T_MONITORING_STATISTICS.RESULT_CHECK,
                        T_MONITORING_STATISTICS.CREATE_DATE
                )
                .from(T_MONITORING_STATISTICS)
                .join(T_MONITORING).on(T_MONITORING_STATISTICS.MONITORING_ID.eq(T_MONITORING.ID))
                .join(T_SERVICE_DATE).on(T_MONITORING.SERVICE_DATE_ID.eq(T_SERVICE_DATE.ID))
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
        MonitoringData monitoring = dsl.select(T_SERVICE_DATE.ID.as("serviceData.id"),
                        T_SERVICE_DATE.NAME.as("serviceData.name"),
                        T_SERVICE_DATE.DESCRIPTION.as("serviceData.description"),
                        T_MONITORING.ID,
                        T_MONITORING.URL,
                        T_MONITORING.SUMMARY,
                        T_MONITORING.IS_ACTIVE
                )
                .from(T_MONITORING)
                .join(T_SERVICE_DATE).on(T_MONITORING.SERVICE_DATE_ID.eq(T_SERVICE_DATE.ID))
                .where(T_MONITORING.URL.eq(url))
                .fetchOneInto(MonitoringData.class);

        if (monitoring == null) {
            throw new EntityNotFoundException(
                    String.format("Ошибка обновления: запись с URL '%s' не найдена!", url),
                    null, MonitoringData.class);
        }

        // Вставляем запись в t_monitoring_statistics
        dsl.insertInto(T_MONITORING_STATISTICS)
                .set(T_MONITORING_STATISTICS.MONITORING_ID, monitoring.getId())
                .set(T_MONITORING_STATISTICS.SERVICE_NAME, monitoring.getServiceData().getName())
                .set(T_MONITORING_STATISTICS.URL, monitoring.getUrl())
                .set(T_MONITORING_STATISTICS.RESULT_CHECK, status)
                .set(T_MONITORING_STATISTICS.CREATE_DATE, LocalDateTime.now())
                .set(T_MONITORING_STATISTICS.DELETED, false)
                .execute();
    }

    public void deactivateStatsByIdMonitoring(Long serviceId) {
        List<Long> monitoringId = dsl.select(T_MONITORING.ID)
                .from(T_MONITORING)
                .join(T_SERVICE_DATE).on(T_MONITORING.SERVICE_DATE_ID.eq(T_SERVICE_DATE.ID))
                .where(T_MONITORING.SERVICE_DATE_ID.eq(serviceId))
                .fetchInto(Long.class);

        monitoringId.forEach(id ->
                dsl.update(T_MONITORING_STATISTICS)
                        .set(T_MONITORING_STATISTICS.DELETED, true)
                        .where(T_MONITORING_STATISTICS.MONITORING_ID.eq(id))
                        .and(T_MONITORING_STATISTICS.DELETED.eq(false))
                        .execute());
    }


    public void deleteMonitoringStatistic() {
        dsl.delete(T_MONITORING_STATISTICS).execute();
    }
}