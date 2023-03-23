package igc.mirror.utils.qfilter;

import org.jooq.Record;
import org.jooq.*;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;

public class QueryBuilder {
    public static Condition buildCondition(Select<? extends Record> query, QueryFilter filter) {
        QueryBuilderImpl q = new QueryBuilderImpl.Builder(query)
                .setFilter(filter)
                .build();

        return q.buildCondition();
    }

    public static Condition buildCondition(Table<? extends Record> T, QueryFilter filter) {
        QueryBuilderImpl q = new QueryBuilderImpl.Builder(T)
                .setFilter(filter)
                .build();

        return q.buildCondition();
    }

    public static List<OrderField<?>> buildSortList(Table<? extends Record> T, Sort sort) {
        QueryBuilderImpl q = new QueryBuilderImpl.Builder(T)
                .setSort(sort)
                .build();

        return q.buildSortList();
    }

    /**
     * Возвращает новый запрос для переданного начального запроса.
     * Формирует дополнительную фразу where по фильтру, пэджинацию и сортировку.
     */
    public static SelectJoinStep<? extends Record> buildQuery(Select<? extends Record> query, QueryFilter filter, Pageable pageable) {

        QueryBuilderImpl q = new QueryBuilderImpl.Builder(query)
                .setFilter(filter)
                .setSort(pageable.getSort())
                .setPageSize(pageable.getPageSize())
                .setOffset(pageable.getOffset())
                .build();

        return q.buildQuery();

    }

    /**
     * Возвращает новый запрос для переданного начального запроса.
     * Формирует дополнительную фразу where по фильтру.
     */
    public static SelectJoinStep<? extends Record> buildQuery(Select<? extends Record> query, QueryFilter filter) {
        QueryBuilderImpl q = new QueryBuilderImpl.Builder(query)
                .setFilter(filter)
                .build();
        return q.buildQuery();

    }

    /**
     * Возвращает новый запрос для переданного начального запроса.
     * Формирует дополнительную фразу where по фильтру.
     */
    public static SelectJoinStep<? extends Record> buildQuery(Select<? extends Record> query, Pageable pageable) {
        QueryBuilderImpl q = new QueryBuilderImpl.Builder(query)
                .setSort(pageable.getSort())
                .setPageSize(pageable.getPageSize())
                .setOffset(pageable.getOffset())
                .build();
        return q.buildQuery();

    }

    /**
     * Возвращает построенный запрос для переданной таблицы.
     * Для его выполнения необходим DSLContext, т.к. класс T - описание таблицы.
     * Пример вызова из контроллера dsl.fetch(buildQuery(...)), dsl.select().from(buildQuery(...))
     */
    public static SelectJoinStep<? extends Record> buildQuery(Table<? extends Record> T, QueryFilter filter, Pageable pageable) {
        QueryBuilderImpl q = new QueryBuilderImpl.Builder(T)
                .setFilter(filter)
                .setSort(pageable.getSort())
                .setPageSize(pageable.getPageSize())
                .setOffset(pageable.getOffset())
                .build();
        return q.buildQuery();

    }

    /**
     * Возвращает построенный запрос для переданной таблицы.
     * Для его выполнения необходим DSLContext, т.к. класс T - описание таблицы.
     * Пример вызова из контроллера dsl.fetch(buildQuery(...)), dsl.select().from(buildQuery(...))
     */
    public static SelectJoinStep<? extends Record> buildQuery(Table<? extends Record> T, QueryFilter filter) {
        QueryBuilderImpl q = new QueryBuilderImpl.Builder(T)
                .setFilter(filter)
                .build();
        return q.buildQuery();

    }

    /**
     * Возвращает построенный запрос для переданной таблицы.
     * Для его выполнения необходим DSLContext, т.к. класс T - описание таблицы.
     * Пример вызова из контроллера dsl.fetch(buildQuery(...)), dsl.select().from(buildQuery(...))
     */
    public static SelectJoinStep<? extends Record> buildQuery(Table<? extends Record> T, Pageable pageable) {
        QueryBuilderImpl q = new QueryBuilderImpl.Builder(T)
                .setSort(pageable.getSort())
                .setPageSize(pageable.getPageSize())
                .setOffset(pageable.getOffset())
                .build();
        return q.buildQuery();

    }

    /**
     * Возвращает новый запрос для получения уникальных значений одного поля из переданного начального запроса.
     * select distinct field from (select ...) t where ... order by field
     */
    public static SelectJoinStep<? extends Record> buildDistinctValueQuery(Select<? extends Record> query, QueryFilter filter) {
        if (filter.getSelectField() == null || filter.getSelectField().isEmpty())
            throw new NullPointerException("Не указано поле для выбора значений");

        QueryBuilderImpl q = new QueryBuilderImpl.Builder(query)
                .setFilter(filter)
                .build();

        return q.buildDistinctValueQuery();

    }

    /**
     * Возвращает новый запрос для переданного начального запроса.
     * Добавляет пэджинацию.
     */
    public static SelectLimitStep<? extends Record> addPageable(SelectLimitStep<? extends Record> query, Pageable pageable) {

        QueryBuilderImpl q = new QueryBuilderImpl.Builder(query)
                .setSort(pageable.getSort())
                .setPageSize(pageable.getPageSize())
                .setOffset(pageable.getOffset())
                .build();

        return q.buildQuery();
    }
}
