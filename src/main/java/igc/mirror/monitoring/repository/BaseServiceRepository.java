package igc.mirror.monitoring.repository;

import igc.mirror.exception.common.EntityDuplicatedException;
import igc.mirror.exception.common.EntityNotFoundException;
import igc.mirror.exception.common.EntityNotSavedException;
import igc.mirror.monitoring.dto.ServiceDataSaveDto;
import igc.mirror.monitoring.dto.ServiceDataUpdateDto;
import igc.mirror.monitoring.model.MonitoringData;
import igc.mirror.monitoring.model.ServiceData;
import jooqdata.tables.TServiceDate;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

import static jooqdata.Tables.T_SERVICE_DATE;
import static org.jooq.impl.DSL.selectOne;

@Repository
@RequiredArgsConstructor
public class BaseServiceRepository {
    private final DSLContext dsl;

    public List<ServiceData> findAllServiceData() {
        return dsl.select(
                        T_SERVICE_DATE.ID,
                        T_SERVICE_DATE.NAME,
                        T_SERVICE_DATE.DESCRIPTION)
                .from(T_SERVICE_DATE)
                .fetchInto(ServiceData.class);
    }

    public ServiceData create(ServiceDataSaveDto data) {

        if (checkExist(data)) {
            throw new EntityDuplicatedException(
                    String.format("Ошибка: сервис с именем %s уже существует!", data.getName()),
                    null, MonitoringData.class
            );
        }
        return save(data);
    }

    private ServiceData save(ServiceDataSaveDto data) {
        return dsl.insertInto(T_SERVICE_DATE)
                .set(T_SERVICE_DATE.NAME, data.getName())
                .set(T_SERVICE_DATE.DESCRIPTION, data.getDescription())
                .onDuplicateKeyUpdate()
                .set(T_SERVICE_DATE.NAME, data.getName())
                .set(T_SERVICE_DATE.DESCRIPTION, data.getDescription())
                .returningResult(T_SERVICE_DATE.ID, T_SERVICE_DATE.NAME, T_SERVICE_DATE.DESCRIPTION)
                .fetchOptional()
                .orElseThrow(() ->
                        new EntityNotSavedException("Ошибка при сохранении данных сервиса в БД", null, ServiceDataSaveDto.class))
                .into(ServiceData.class);
    }

    private boolean checkExist(ServiceDataSaveDto data) {
        return dsl
                .fetchExists(selectOne()
                        .from(T_SERVICE_DATE)
                        .where(TServiceDate.T_SERVICE_DATE.NAME.eq(data.getName()))
                );
    }

    public ServiceData updateInDb(Map<Field<?>, Object> updateFields, Long id) {
        return dsl.update(T_SERVICE_DATE)
                .set(updateFields)
                .where(T_SERVICE_DATE.ID.eq(id))
                .returningResult(T_SERVICE_DATE.ID, T_SERVICE_DATE.NAME, T_SERVICE_DATE.DESCRIPTION)
                .fetchOptional()
                .orElseThrow(() ->
                        new EntityNotSavedException("Ошибка при обновлении данных сервиса в БД", null, ServiceDataUpdateDto.class))
                .into(ServiceData.class);
    }

    public ServiceData findServiceById(Long id) {
        return dsl.selectFrom(T_SERVICE_DATE)
                .where(T_SERVICE_DATE.ID.eq(id))
                .fetchOptional()
                .orElseThrow(() ->
                        new EntityNotFoundException(String.format("Ошибка при поиске записи сервиса в БД. Запись с id = %s не существует ", id), id, ServiceData.class))
                .into(ServiceData.class);
    }


    /**
     * Удаляет запись сервиса
     *
     * @param id идентификатор записи
     */
    public void deleteServiceById(Long id) {
        findServiceById(id);
        dsl.delete(T_SERVICE_DATE)
                .where(T_SERVICE_DATE.ID.eq(id))
                .execute();
    }

}