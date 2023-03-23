package igc.mirror.utils.qfilter;

import org.jooq.Record;
import org.jooq.*;
import org.jooq.impl.DSL;
import org.jooq.impl.SQLDataType;
import org.springframework.data.domain.Sort;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.jooq.impl.DSL.*;

public class QueryBuilderImpl {
    private List<Field<?>> columns;

    private Map<String, Field<?>> fields;

    private Table<? extends Record> fromTable;

    private Select<? extends Record> fromOrigin;

    private QueryFilter filter;

    private Sort sort;

    private Integer pageSize;

    private Long offset;

    public static class Builder {
        private List<Field<?>> columns;

        private Table<? extends Record> fromTable;

        private Select<? extends Record> fromOrigin;

        private QueryFilter filter;

        private Sort sort;

        private Long offset;

        private Integer pageSize;

        public Builder(Table<? extends Record> T) {
            this.columns = Arrays.asList(T.fields());
            this.fromTable = T;
        }

        public Builder(Select<? extends Record> query) {
            this.columns = query.getSelect().stream().toList();
            this.fromOrigin = query;
        }

        public Builder setFilter(QueryFilter filter) {
            this.filter = filter;
            if (filter != null && fromOrigin != null) {
                this.fromTable = fromOrigin.asTable("t");

                // Todo можно в будущем написать для перечисленных в фильтре полей
                // columns = select().from(query.asTable("t")).getSelect().stream().toList();
                columns = Arrays.asList(fromTable.fields());
                this.fromOrigin = null;
            }
            return this;
        }

        public Builder setSort(Sort sort) {
            this.sort = sort;
            return this;
        }

        public Builder setPageSize(int pageSize) {
            this.pageSize = pageSize;
            return this;
        }

        public Builder setOffset(long offset) {
            this.offset = offset;
            return this;
        }

        public QueryBuilderImpl build() {
            return new QueryBuilderImpl(this);
        }
    }

    private QueryBuilderImpl(Builder builder) {
        this.columns = builder.columns;
        this.fromTable = builder.fromTable;
        this.fromOrigin = builder.fromOrigin;
        this.filter = builder.filter;
        this.sort = builder.sort;
        this.pageSize = builder.pageSize;
        this.offset = builder.offset;
        setFields();
    }

    private void setFields() {
        this.fields = this.columns
                .stream()
                .collect(Collectors.toMap((k -> k.getName().toLowerCase().replace("_", "")), Function.identity()));
    }

    public Field<?> getTableField(String name) {
        return fields.get(name.toLowerCase());
    }

    private <T> Object convertStringValue(String value, Class<T> toClass) {
        if (toClass == String.class)
            return value;
        else if (toClass == Long.class)
            return Long.valueOf(value);
        else if (toClass == BigDecimal.class)
            return new BigDecimal(value);
        else if (toClass == Integer.class)
            return Integer.valueOf(value);
        else if (toClass == LocalDateTime.class) {
            return LocalDateTime.parse(value);
        } else
            return null;
    }

    private Field<?> convertToString(Field<?> field, String format) {
        if (field.getType() == String.class)
            return field;
        else if (field.getType() == LocalDateTime.class) {
            return DSL.field("to_char({0}, {1})", SQLDataType.VARCHAR, field, DSL.inline(format));
        } else if (field.getType() == BigDecimal.class &&
                format != null && !format.isEmpty())
            return DSL.field("to_char({0}, {1})", SQLDataType.VARCHAR, field, DSL.inline(format));

        return field.cast(String.class);
    }

    private Condition buildEqualCondition(Field<?> field, String value, String format) {
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

    private Condition buildSingleCondition(QueryCondition condition) {
        Condition singleCondition = noCondition();

        if (condition.getFilter() != null)
            return buildCondition(condition.getFilter());

        Field<?> field = getTableField(condition.getField());

        switch (condition.getOperator()) {
            case LIKE -> singleCondition = convertToString(field, condition.getFormat()).like(condition.getValue());

            case NOT_LIKE ->
                    singleCondition = convertToString(field, condition.getFormat()).notLike(condition.getValue());

            case LIKE_IGNORE_CASE ->
                    singleCondition = convertToString(field, condition.getFormat()).likeIgnoreCase(condition.getValue());

            case NOT_LIKE_IGNORE_CASE ->
                    singleCondition = convertToString(field, condition.getFormat()).notLikeIgnoreCase(condition.getValue());

            case EQ ->
                    singleCondition = buildEqualCondition(field, condition.getValue(), condition.getFormat());

            case NOT_EQ ->
                    singleCondition = field.coerce(Object.class).notEqual(convertStringValue(condition.getValue(), field.getType()));

            case GT ->
                    singleCondition = field.coerce(Object.class).greaterThan(convertStringValue(condition.getValue(), field.getType()));

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

    private Condition buildCondition(QueryFilter filter) {
        Condition where = noCondition();
        if (filter != null && filter.getCond() != null && !filter.getCond().isEmpty()) {
            switch (filter.getLogic()) {
                case OR:
                    for (QueryCondition condition : filter.getCond()) {
                        where = where.or(buildSingleCondition(condition));
                    }
                    break;
                default:
                    for (QueryCondition condition : filter.getCond()) {
                        where = where.and(buildSingleCondition(condition));
                    }
            }
        }
        return where;
    }

    public Condition buildCondition() {
        Condition where = noCondition();
        if (filter != null && filter.getCond() != null && !filter.getCond().isEmpty()) {
            switch (filter.getLogic()) {
                case OR:
                    for (QueryCondition condition : filter.getCond()) {
                        where = where.or(buildSingleCondition(condition));
                    }
                    break;
                default:
                    for (QueryCondition condition : filter.getCond()) {
                        where = where.and(buildSingleCondition(condition));
                    }
            }
        }
        return where;
    }

    public List<OrderField<?>> buildSortList() {
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
     * Возвращает построенный запрос для переданной таблицы.
     * Для его выполнения необходим DSLContext, т.к. класс T - описание таблицы.
     * Пример вызова из контроллера dsl.fetch(buildQuery(...)), dsl.select().from(buildQuery(...))
     */
    public SelectJoinStep<? extends Record> buildQuery() {

        SelectJoinStep<? extends Record> query =
                (this.fromTable != null ? select().from(this.fromTable) : (SelectJoinStep<? extends Record>) this.fromOrigin);

        query.where(buildCondition());

        if (this.sort != null)
            query.orderBy(buildSortList());

        if (this.pageSize != null)
            query.limit(pageSize)
                    .offset(offset);

        return query;
    }

    /**
     * Возвращает новый запрос для получения уникальных значений одного поля из переданного начального запроса.
     * select distinct field from (select ...) t where ... order by field
     */
    public SelectJoinStep<? extends Record> buildDistinctValueQuery() {
        if (filter.getSelectField() == null || filter.getSelectField().isEmpty())
            throw new NullPointerException("Не указано поле для выбора значений");

        return
                (SelectJoinStep<? extends Record>) selectDistinct(getTableField(filter.getSelectField()))
                        .from(fromTable)
                        .where(buildCondition())
                        .orderBy(getTableField(filter.getSelectField()).asc().nullsLast());
    }
}
