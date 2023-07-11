package igc.mirror.variable.repository;

import igc.mirror.variable.dto.VariableDto;
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
}
