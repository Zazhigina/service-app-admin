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

    public TExternalSourceRecord createExternalSourceRecord(ExternalSource externalSource) {
        return dsl
                .insertInto(T_EXTERNAL_SOURCE)
                .set(dsl.newRecord(T_EXTERNAL_SOURCE, externalSource))
                .returning()
                .fetchOptional()
                .orElseThrow(() -> new EntityNotSavedException("Возникли ошибки при сохранении данных", null, ExternalSourceDto.class));
    }

    public void changeExternalSource(ExternalSource externalSource) {
        int count = dsl.update(T_EXTERNAL_SOURCE)
                .set(T_EXTERNAL_SOURCE.CODE, externalSource.getCode())
                .set(T_EXTERNAL_SOURCE.NAME, externalSource.getName())
                .set(T_EXTERNAL_SOURCE.DESCRIPTION, externalSource.getDescription())
                .set(T_EXTERNAL_SOURCE.DELETED, externalSource.isDeleted())
                .set(T_EXTERNAL_SOURCE.LAST_UPDATE_USER, externalSource.getLastUpdateUser())
                .set(T_EXTERNAL_SOURCE.LAST_UPDATE_DATE, externalSource.getLastUpdateDate())
                .where(T_EXTERNAL_SOURCE.ID.eq(externalSource.getId()))
                .execute();

        if (count == 0)
            throw new EntityNotFoundException("Объект не найден", externalSource.getId(), ExternalSource.class);
    }

    public void markDeletedById(Long id, String userName, LocalDateTime lastUpdateDate, boolean isDeleted) {
        int count = dsl.update(T_EXTERNAL_SOURCE)
                .set(T_EXTERNAL_SOURCE.DELETED, isDeleted)
                .set(T_EXTERNAL_SOURCE.LAST_UPDATE_USER,userName)
                .set(T_EXTERNAL_SOURCE.LAST_UPDATE_DATE, lastUpdateDate)
                .where(T_EXTERNAL_SOURCE.ID.eq(id))
                .execute();

        if (count == 0)
            throw new EntityNotFoundException("Объект не найден", id, ExternalSource.class);
    }

    public List<ExternalSource> getAllExternalSources() {
        return dsl.selectFrom(T_EXTERNAL_SOURCE).fetchInto(ExternalSource.class);
    }

//    public ExternalSource switchDeletedById(Long id, String userName, LocalDateTime lastUpdateDate) {
//        ExternalSource externalSource = findById(id);
//        if (externalSource != null) {
//            externalSource.setDeleted(!externalSource.isDeleted());
//            externalSource.setLastUpdateUser(userName);
//            externalSource.setLastUpdateDate(lastUpdateDate);
//
//            markDeletedById(externalSource.getId(), externalSource.getLastUpdateUser(), externalSource.getLastUpdateDate());
//        }
//
//        return externalSource;
//    }
}
