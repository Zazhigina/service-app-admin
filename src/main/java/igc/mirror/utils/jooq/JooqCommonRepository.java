package igc.mirror.utils.jooq;

import igc.mirror.utils.qfilter.DataFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface JooqCommonRepository<T, I>  {
    /**
     * Поиск сущности по идентификатору(ID)
     *
     * @param identifier ID
     * @return Искомая сущность
     */
    T find(I identifier);

    /**
     *
     * @param dataFilter набор критериев поиска
     * @param pageable настройки пэджинации и сортировки
     * @return Список искомых сущностей
     */
    Page<T> findByFilters(DataFilter<?> dataFilter, Pageable pageable);

    /**
     *
     * @param dataFilter набор критериев поиска
     * @return Список искомых сущностей
     */
    List<T> findByFilters(DataFilter<?> dataFilter);

    /**
     * Проверка существования сущности по идентификатору
     *
     * @param identifier ID
     * @return Boolean
     */
    Boolean checkExist(I identifier);

    /**
     * Сохранение сущности
     *
     * @param data Данные для сохранения
     * @return Сохраненная сущность
     */
    T save(T data);

    /**
     * Сохранение списка сущностей
     *
     * @param dataList Список сущностей для сохранения
     * @return Список сохраненных сущностей
     */
    List<T> saveList(List<T> dataList);

    /**
     * Удаление сущности по ID
     *
     * @param identifier ID
     */
    void delete(I identifier);

    /**
     * Удаление сущностей по ID
     *
     * @param identifierList Список ID
     */
    void deleteList(List<I> identifierList);

}