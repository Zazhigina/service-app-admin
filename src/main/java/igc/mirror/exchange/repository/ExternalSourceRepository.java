package igc.mirror.exchange.repository;

import igc.mirror.exception.common.EntityNotFoundException;
import igc.mirror.exception.common.EntityNotSavedException;
import igc.mirror.exchange.dto.ExternalSourceDto;
import igc.mirror.exchange.model.ExternalSource;
import jooqdata.tables.records.TExternalSourceRecord;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

import static jooqdata.tables.TExternalSource.T_EXTERNAL_SOURCE;

@Repository
@RequiredArgsConstructor
public class ExternalSourceRepository {
    private final DSLContext dsl;

    public ExternalSource findById(Long id) {
        return dsl.selectFrom(T_EXTERNAL_SOURCE)
                .where(T_EXTERNAL_SOURCE.ID.eq(id))
                .fetchOneInto(ExternalSource.class);
    }

    private TExternalSourceRecord createExternalSourceRecord(ExternalSource externalSource) {
        return dsl
                .insertInto(T_EXTERNAL_SOURCE)
                .set(dsl.newRecord(T_EXTERNAL_SOURCE, externalSource))
                .returning()
                .fetchOptional()
                .orElseThrow(() -> new EntityNotSavedException("Возникли ошибки при сохранении данных", null, ExternalSourceDto.class));
    }

    public Long createExternalSource(ExternalSource externalSource) {
        return createExternalSourceRecord(externalSource).getId();
    }

    public void saveExternalSource(ExternalSource model) {
        int count = dsl.update(T_EXTERNAL_SOURCE)
                .set(T_EXTERNAL_SOURCE.CODE, model.getCode())
                .set(T_EXTERNAL_SOURCE.NAME, model.getName())
                .set(T_EXTERNAL_SOURCE.DESCRIPTION, model.getDescription())
                .set(T_EXTERNAL_SOURCE.DELETED, model.isDeleted())
                .set(T_EXTERNAL_SOURCE.LAST_UPDATE_USER, model.getLastUpdateUser())
                .set(T_EXTERNAL_SOURCE.LAST_UPDATE_DATE, LocalDateTime.now())
                .where(T_EXTERNAL_SOURCE.ID.eq(model.getId()))
                .execute();

        if (count == 0)
            throw new EntityNotFoundException("Объект не найден", model.getId(), ExternalSource.class);
    }

    public void markDeletedById(Long id, String userName) {
        int count = dsl.update(T_EXTERNAL_SOURCE)
                .set(T_EXTERNAL_SOURCE.DELETED, true)
                .set(T_EXTERNAL_SOURCE.LAST_UPDATE_USER,userName)
                .set(T_EXTERNAL_SOURCE.LAST_UPDATE_DATE, LocalDateTime.now())
                .where(T_EXTERNAL_SOURCE.ID.eq(id))
                .execute();

        if (count == 0)
            throw new EntityNotFoundException("Объект не найден", id, ExternalSource.class);
    }

    public List<ExternalSource> getAllExternalSources() {
        return dsl.selectFrom(T_EXTERNAL_SOURCE).fetchInto(ExternalSource.class);
    }
}
