package igc.mirror.variable.repository;

import igc.mirror.exception.common.EntityNotFoundException;
import igc.mirror.variable.dto.VariableDto;
import igc.mirror.variable.model.Variable;
import jooqdata.tables.TVariable;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Repository
public class VariableRepository {

    @Autowired
    private DSLContext dsl;

    private static final TVariable VARIABLE = TVariable.T_VARIABLE;

    /**
     * Находит переменные по указанным идентификаторам (если ИД не указаны, возвращает все)
     *
     * @param variableIds список ИД переменных
     * @return список переменных
     */
    public List<VariableDto> findVariables(List<Long> variableIds) {
        Condition condition = !CollectionUtils.isEmpty(variableIds) ? VARIABLE.ID.in(variableIds) : DSL.noCondition();

        return dsl.selectFrom(VARIABLE)
                .where(condition)
                .fetchInto(VariableDto.class);
    }

    /**
     * Обновляет переменную в БД
     *
     * @param variable измененная переменная
     */
    public void updateVariable(Variable variable) {
        int count = dsl.update(VARIABLE)
                .set(VARIABLE.DEFAULT_VAL, variable.getDefaultVal())
                .set(VARIABLE.LAST_UPDATE_DATE, variable.getLastUpdateDate())
                .set(VARIABLE.LAST_UPDATE_USER, variable.getLastUpdateUser())
                .where(VARIABLE.ID.eq(variable.getId()))
                .execute();
        if (count == 0)
            throw new EntityNotFoundException(variable.getId(), Variable.class);
    }
}
