package igc.mirror.repository;

import igc.mirror.exception.common.EntityNotSavedException;
import igc.mirror.model.LetterTemplate;
import igc.mirror.model.LetterTemplateVariable;
import jooqdata.tables.TLetterTemplateVariable;
import jooqdata.tables.records.TLetterTemplateVariableRecord;
import org.jooq.DSLContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

@Repository
public class LetterTemplateVariableRepository {
    static final Logger logger = LoggerFactory.getLogger(LetterTemplateVariableRepository.class);

    public static final TLetterTemplateVariable LETTER_TEMPLATE_VAR = TLetterTemplateVariable.T_LETTER_TEMPLATE_VARIABLE;

    @Autowired
    DSLContext dsl;

    /**
     * Сохраняет переменную шаблона письма в БД
     *
     * @param letterTemplateVariable переменная шаблона письма
     * @return строка БД сохраненной переменной
     */
    private TLetterTemplateVariableRecord createLetterTemplateVariableRecord(LetterTemplateVariable letterTemplateVariable) {
        return dsl.insertInto(TLetterTemplateVariable.T_LETTER_TEMPLATE_VARIABLE)
                .set(dsl.newRecord(TLetterTemplateVariable.T_LETTER_TEMPLATE_VARIABLE, letterTemplateVariable))
                .returning()
                .fetchOne();
    }

    /**
     * Сохраняет переменную шаблона письма в БД
     *
     * @param letterTemplateVariable параметр шаблона письма
     * @return сохраненная переменная
     */
    public LetterTemplateVariable createLetterTemplateVariable(LetterTemplateVariable letterTemplateVariable) {
        TLetterTemplateVariableRecord letterTemplateVariableRecord = createLetterTemplateVariableRecord(letterTemplateVariable);
        if (letterTemplateVariableRecord == null || letterTemplateVariableRecord.getId() == null) {
            logger.error("Не создана переменная для шаблона письма letterTemplateId {}, имя {}, значение {}",
                    letterTemplateVariable.getLetterTemplateId(), letterTemplateVariable.getName(), letterTemplateVariable.getVal());
            throw new EntityNotSavedException(null, LetterTemplateVariable.class);
        }
        letterTemplateVariable.setId(letterTemplateVariableRecord.getId());
        return letterTemplateVariable;
    }

    /**
     * Получает переменные шаблона письма в БД
     *
     * @param letterTemplateId идентифиатор шаблона письма
     * @return список переменных
     */
    public List<LetterTemplateVariable> getLetterTemplateVariables(Long letterTemplateId) {
        return dsl.selectFrom(LETTER_TEMPLATE_VAR)
                .where(LETTER_TEMPLATE_VAR.LETTER_TEMPLATE_ID.equal(letterTemplateId))
                .fetchInto(LetterTemplateVariable.class);
    }

    /**
     * Удаляет все переменные шаблона письма в БД
     *
     * @param letterTemplateId идентификатор шаблона письма
     */
    public void deleteLetterTemplateVariable(Long letterTemplateId) {
        int deleted =
                dsl.deleteFrom(LETTER_TEMPLATE_VAR)
                        .where(LETTER_TEMPLATE_VAR.LETTER_TEMPLATE_ID.equal(letterTemplateId))
                        .execute();
    }

    /**
     * Удаляет переменные шаблона письма по списку Id
     *
     * @param variableIds список идентификаторов переменных шаблона письма
     */
    public void deleteLetterTemplateVariable(List<Long> variableIds) {
        int deleted =
                dsl.deleteFrom(LETTER_TEMPLATE_VAR)
                        .where(LETTER_TEMPLATE_VAR.ID.in(variableIds))
                        .execute();
    }

    /**
     * Синхронизирует новые переменные шаблона письма с хранимыми в БД
     *
     * @param letterTemplate шаблон письма
     */
    public void synchronizeLetterTemplateVariable(LetterTemplate letterTemplate) {
        List<LetterTemplateVariable> existedVariables = dsl
                .selectFrom(LETTER_TEMPLATE_VAR)
                .where(LETTER_TEMPLATE_VAR.LETTER_TEMPLATE_ID.equal(letterTemplate.getId()))
                .fetchInto(LetterTemplateVariable.class);

        List<LetterTemplateVariable> newVariables = letterTemplate.getVariables();

        List<Long> deleteVariables = new ArrayList<>();
        List<LetterTemplateVariable> insertVariables = new ArrayList<>();

        if (existedVariables.isEmpty() && !CollectionUtils.isEmpty(newVariables)) {
            insertVariables.addAll(newVariables);
        } else if (!existedVariables.isEmpty() && CollectionUtils.isEmpty(newVariables)) {
            deleteLetterTemplateVariable(letterTemplate.getId());
        } else if (!CollectionUtils.isEmpty(existedVariables) && !CollectionUtils.isEmpty(newVariables)) {
            // сортируем на новые и те, которые нужно удалить
            newVariables.forEach(p -> {
                if (!existedVariables.contains(p)) {
                    insertVariables.add(p);
                }
            });

            existedVariables.forEach(p -> {
                if (!newVariables.contains(p)) {
                    deleteVariables.add(p.getId());
                }
            });
        } // else оба списка пустые

        if (!insertVariables.isEmpty())
            insertVariables.forEach(this::createLetterTemplateVariable);

        if (!deleteVariables.isEmpty())
            deleteLetterTemplateVariable(deleteVariables);
    }
}
