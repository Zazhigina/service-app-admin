package igc.mirror.appcontrol.repository;

import igc.mirror.appcontrol.dto.AppControlDto;
import igc.mirror.appcontrol.dto.AppControlEnabledAllDto;
import igc.mirror.exception.common.EntityNotFoundException;
import jooqdata.tables.TAppControl;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

import static org.jooq.impl.DSL.selectOne;

@Repository
public class AppControlRepository {

    @Autowired
    private DSLContext dsl;

    private static final TAppControl APP_CONTROL = TAppControl.T_APP_CONTROL;

    /**
     * Находит сервисы по указанным идентификаторам (если ИД не указаны, возвращает все)
     *
     * @param name название сервиса
     * @return список сервисов
     */
    public List<AppControlDto> findAppControl(String name) {
        Condition condition = name != null && !name.isEmpty() ? APP_CONTROL.NAME.eq(name) : DSL.noCondition();

        return dsl.selectFrom(APP_CONTROL)
                .where(condition)
                .fetchInto(AppControlDto.class);
    }



    public Boolean checkExist(String name) {
        return dsl.fetchExists(
                selectOne()
                        .from(APP_CONTROL)
                        .where(APP_CONTROL.NAME.eq(name))
        );
    }

    public AppControlDto save(AppControlDto data) {
        return dsl.insertInto(TAppControl.T_APP_CONTROL)
                .set(dsl.newRecord(TAppControl.T_APP_CONTROL, data))
                .onDuplicateKeyUpdate()
                .set(TAppControl.T_APP_CONTROL.DESCRIPTION, data.getDescription())
                .set(TAppControl.T_APP_CONTROL.ENABLED, data.getEnabled())
                .set(TAppControl.T_APP_CONTROL.LAST_UPDATE_USER, data.getLastUpdateUser())
                .returningResult(TAppControl.T_APP_CONTROL.fields())
                .fetchOptional()
                .map(r -> r.into(AppControlDto.class))
                .orElseThrow(() -> new EntityNotFoundException(null, AppControlDto.class));
    }

    public List<AppControlDto> setEnabledAll(AppControlEnabledAllDto enabledAllDto, String user) {
        return dsl.update(TAppControl.T_APP_CONTROL)
                .set(TAppControl.T_APP_CONTROL.ENABLED, enabledAllDto.getEnabled())
                .set(TAppControl.T_APP_CONTROL.LAST_UPDATE_USER, user)
                .returningResult(TAppControl.T_APP_CONTROL.fields())
                .fetchInto(AppControlDto.class);
    }

}
