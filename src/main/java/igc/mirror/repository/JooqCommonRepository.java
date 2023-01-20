package igc.mirror.repository;

import igc.mirror.utils.qfilter.DataFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface JooqCommonRepository<T, I> {
    T find(I identifier);

    Page<T> findByFilters(DataFilter<?> dataFilter, Pageable pageable);

    List<T> findByFilters(DataFilter<?> dataFilter);

    Boolean checkExist(I identifier);

    T save(T data);

    List<T> saveList(List<T> dataList);

    void delete(I identifier);

    void deleteList(List<I> identifierList);

}
