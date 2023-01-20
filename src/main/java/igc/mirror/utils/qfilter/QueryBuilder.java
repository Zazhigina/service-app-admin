package igc.mirror.utils.qfilter;

import org.jooq.Record;
import org.jooq.*;
import org.jooq.impl.DSL;
import org.jooq.impl.SQLDataType;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.jooq.impl.DSL.noCondition;
import static org.jooq.impl.DSL.select;

public class QueryBuilder {
    private static List<Field<?>> columns;

    private static Field<?> getTableField(String name) {
        for (Field<?> col : columns) {
            String simpleName = col.getName().toLowerCase().replace("_", "");
            //  System.out.println(simpleName);
            if (simpleName.equals(name.toLowerCase()))
                return col;
        }
        return null;
    }

    private static <T> Object convertStringValue(String value, Class<T> toClass) {
        if (toClass == String.class)
            return value;
        else if (toClass == Long.class)
            return Long.valueOf(value);
        else if (toClass == BigDecimal.class)
            return new BigDecimal(value);
        else if(toClass == OffsetDateTime.class)
            return  OffsetDateTime.parse(value); // добавил OffsetDateTime t
        else if (toClass == LocalDateTime.class) {
            return LocalDateTime.parse(value);
        } else
            return null;
    }

    private static Field<?> convertToString(Field<?> field, String format) {
        if (field.getType() == String.class)
            return field;
        else if (field.getType() == LocalDateTime.class) {
            return DSL.field("to_char({0}, {1})", SQLDataType.VARCHAR, field, DSL.inline(format));
        } else if (field.getType() == BigDecimal.class &&
                format != null && !format.isEmpty())
            return DSL.field("to_char({0}, {1})", SQLDataType.VARCHAR, field, DSL.inline(format));

        return field.cast(String.class);
    }

    private static Condition buildEqualCondition(Field<?> field, String value, String format) {
        Condition condition = noCondition();
        if (field.getType() == LocalDateTime.class) {
            LocalDateTime dateValue = LocalDateTime.parse(value);
            if (format != null)
                condition = DSL.trunc(field.coerce(LocalDateTime.class), DatePart.valueOf(format.toUpperCase())).equal(dateValue);
            else
                condition = field.coerce(LocalDateTime.class).equal(dateValue);
        } else
            condition = field.coerce(Object.class).equal(convertStringValue(value, field.getType()));
        return condition;

    }

    private static Condition buildSingleCondition(QueryCondition condition) {
        Condition singleCondition = noCondition();

        if (condition.getFilter() != null)
            return buildCondition(condition.getFilter());

        Field<?> field = getTableField(condition.getField());

        //Field typeField = field.coerce(field.getType());

        switch (condition.getOperator()) {
            case LIKE -> singleCondition = convertToString(field, condition.getFormat()).like(condition.getValue());

            case NOT_LIKE ->
                    singleCondition = convertToString(field, condition.getFormat()).notLike(condition.getValue());

            case LIKE_IGNORE_CASE ->
                    singleCondition = convertToString(field, condition.getFormat()).likeIgnoreCase(condition.getValue());

            case NOT_LIKE_IGNORE_CASE ->
                    singleCondition = convertToString(field, condition.getFormat()).notLikeIgnoreCase(condition.getValue());

            case EQ ->
//                  singleCondition = field.coerce(Object.class).equal(convertStringValue(condition.getValue(), field.getType()));
                    singleCondition = buildEqualCondition(field, condition.getValue(), condition.getFormat());

            case NOT_EQ ->
                    singleCondition = field.coerce(Object.class).notEqual(convertStringValue(condition.getValue(), field.getType()));

            case GT ->
//            {
//                System.out.println(field);
//                System.out.println(field.coerce(Long.class));
//                System.out.println(field.getDataType());
//                System.out.println(field2.getType());
//                singleCondition = field.coerce(Long.class).gt(Long.valueOf(condition.getValue()));
//                singleCondition = field2.greaterThan(5L);
                    singleCondition = field.coerce(Object.class).greaterThan(convertStringValue(condition.getValue(), field.getType()));
//                singleCondition = field.coerce(field.getType()).greaterThan(convertStringValue(condition.getValue(), field.getType()));
//            }
            case GTE ->
                    singleCondition = field.coerce(Object.class).greaterOrEqual(convertStringValue(condition.getValue(), field.getType()));

            case LT ->
                    singleCondition = field.coerce(Object.class).lessThan(convertStringValue(condition.getValue(), field.getType()));

            case LTE ->
                    singleCondition = field.coerce(Object.class).lessOrEqual(convertStringValue(condition.getValue(), field.getType()));

            case NULL -> singleCondition = field.isNull();

            case NOT_NULL -> singleCondition = field.isNotNull();
        }
        return singleCondition;
    }

    public static Condition buildCondition(Select<? extends Record> query, QueryFilter filter) {
        columns = query.getSelect();
        return buildCondition(filter);
    }

    public static Condition buildCondition(Table<? extends Record> T, QueryFilter filter) {
        columns = select().from(T).getSelect();
        return buildCondition(filter);
    }


    private static Condition buildCondition(QueryFilter filter) {
        Condition where = noCondition();
        if (filter != null && filter.getCond() != null && !filter.getCond().isEmpty()) {
//            System.out.println("build where");
//            System.out.println(filter.getLogicalOperator());
            switch (filter.getLogic()) {
                case OR:
                    for (QueryCondition condition : filter.getCond()) {
                        where = where.or(buildSingleCondition(condition));
//                       System.out.println(where);
                    }
                    break;
                default:
                    for (QueryCondition condition : filter.getCond()) {
                        where = where.and(buildSingleCondition(condition));
//                        System.out.println(where);
                    }
            }
        }
        return where;
    }

    public static List<OrderField<?>> buildSortList(Table<? extends Record> T, Sort sort) {
        columns = select().from(T).getSelect();
        List<OrderField<?>> orderFields = new ArrayList<>();
        for (Sort.Order order : sort) {
            Field<?> field = getTableField(order.getProperty());
            if (order.getDirection().equals(Sort.Direction.ASC))
                orderFields.add(field.asc().nullsLast());
            else
                orderFields.add(field.desc().nullsFirst());
        }
        return orderFields;
    }

    private static List<OrderField<?>> buildSortList(Sort sort) {
        List<OrderField<?>> orderFields = new ArrayList<>();
        for (Sort.Order order : sort) {
            Field<?> field = getTableField(order.getProperty());
            if (order.getDirection().equals(Sort.Direction.ASC))
                orderFields.add(field.asc().nullsLast());
            else
                orderFields.add(field.desc().nullsFirst());
        }
        return orderFields;
    }

    /**
     * Возвращает фразу select как обертку для начального запроса.
     * select * from (select ..) t
     */
    private static SelectJoinStep<? extends Record> buildSelect(Select<? extends Record> query, QueryFilter filter) {

        SelectJoinStep<? extends Record> resultQuery;
        //       DSLContext dsl = query.configuration().dsl();

        // переданый запрос интерпретируем, как таблицу select from (T)
        if (filter != null) {

            //           Table<? extends Record> T = query.asTable("t");

            //           columns = Arrays.asList(T.fields());

//            SelectJoinStep<? extends Record> resultQuery = dsl.select().from(T);

//            resultQuery = dsl.select().from(T);
            //           resultQuery = select().from(T);
            resultQuery = select().from(query.asTable("t"));

            // Todo можно в будущем написать для перечисленных в фильтре полей

            // return resultQuery;
        } else {
            // columns = query.getSelect().stream().toList();
            resultQuery = (SelectJoinStep<? extends Record>) query;
            // resultQuery = select().from(query);
//            resultQuery =(SelectJoinStep<? extends Record>) DSL.select(query.getSelect()).from(query.$from()).where(query.$where());

        }
        columns = resultQuery.getSelect().stream().toList();
        return resultQuery;

    }

    /**
     * Возвращает фразу select distinct с переданным полем, как обертку для начального запроса.
     * select distinct field from (select) t
     */
    private static SelectJoinStep<? extends Record> buildDistinctSelect(Select<? extends Record> query, QueryFilter filter) {

        DSLContext dsl = query.configuration().dsl();

        // переданый запрос интерпретируем, как таблицу select from (T)
        Table<? extends Record> T = query.asTable("t");

        columns = Arrays.asList(T.fields());

        SelectJoinStep<? extends Record> resultQuery = null;
        if (filter.getSelectField() != null && !filter.getSelectField().isEmpty())
            resultQuery = dsl.selectDistinct(getTableField(filter.getSelectField())).from(T);

        // Todo можно в будущем написать для нескольких полей

        return resultQuery;

    }

    /**
     * Возвращает новый запрос для переданного начального запроса.
     * Формирует дополнительную фразу where по фильтру.
     */
    public static SelectJoinStep<? extends Record> buildQuery(Select<? extends Record> query, QueryFilter filter) {
        // основной запрос
        SelectJoinStep<? extends Record> resultQuery = buildSelect(query, filter);

        // условие
        resultQuery.where(buildCondition(filter));

        return resultQuery;

    }

    /**
     * Возвращает новый запрос для переданного начального запроса.
     * Формирует дополнительную фразу where по фильтру, пэджинацию и сортировку.
     */
    public static SelectJoinStep<? extends Record> buildQuery(Select<? extends Record> query, QueryFilter filter, Pageable pageable) {

        // основной запрос
        //       SelectJoinStep<? extends Record> resultQuery = buildQuery(query, filter);
        SelectJoinStep<? extends Record> resultQuery = buildSelect(query, filter);

        // условие
        resultQuery.where(buildCondition(filter));

        // сортировка и пэджинации
        if (pageable.getSort() != null)
            resultQuery.orderBy(buildSortList(pageable.getSort()));

        resultQuery.limit(pageable.getPageSize())
                .offset(pageable.getOffset());

        return resultQuery;

    }

    /**
     * Возвращает новый запрос для получения уникальных значений одного поля из переданного начального запроса.
     * select distinct field from (select ...) t where ... order by field
     */
    public static SelectJoinStep<? extends Record> buildDistinctValueQuery(Select<? extends Record> query, QueryFilter filter) {
        if (filter.getSelectField() == null || filter.getSelectField().isEmpty())
            throw new NullPointerException("Не указано поле для выбора значений");

        // основной запрос
        SelectJoinStep<? extends Record> resultQuery = buildDistinctSelect(query, filter);

        // условие
        resultQuery.where(buildCondition(filter));

        // сортировка по полю
        resultQuery.orderBy(getTableField(filter.getSelectField()).asc().nullsLast());

        return resultQuery;

    }

    /**
     * Возвращает построенный запрос для переданной таблицы.
     * Для его выполнения необходим DSLContext, т.к. класс T - описание таблицы.
     * Пример вызова из контроллера dsl.fetch(buildQuery(...)), dsl.select().from(buildQuery(...))
     */
    public static SelectJoinStep<? extends Record> buildQuery(Table<? extends Record> T, QueryFilter filter, Pageable pageable) {

        columns = Arrays.asList(T.fields());


        SelectJoinStep<? extends Record> query = select().from(T);

        // SelectJoinStep<? extends Record> query = (SelectJoinStep<? extends Record>) DSL.selectFrom(T);

        Condition wherePhrase = buildCondition(filter);

        query.where(wherePhrase);

        if (pageable.getSort() != null)
            query.orderBy(buildSortList(pageable.getSort()));

        query.limit(pageable.getPageSize())
                .offset(pageable.getOffset());

        //    System.out.println(query.getSQL());

        return query;

    }

    /**
     * Возвращает новый запрос для переданного начального запроса.
     * Добавляет пэджинацию.
     */
    public static SelectLimitStep<? extends Record> addPageable(SelectLimitStep<? extends Record> query, Pageable pageable) {

        SelectLimitStep<? extends Record> resultQuery = query;

        resultQuery.limit(pageable.getPageSize())
                .offset(pageable.getOffset());

        return resultQuery;
    }

}
