package igc.mirror.service;

import igc.mirror.dto.ParamDto;
import igc.mirror.dto.ParamEditableDto;
import igc.mirror.utils.qfilter.DataFilter;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ParamService {
    /**
     * Возвращает список параметров
     *
     * @param dataFilter набор критериев поиска
     * @param pageable настройки пэджинации и сортировки
     * @return список параметров
     */
    Page<ParamDto> findParamsByFilters(DataFilter<?> dataFilter, Pageable pageable);

    /**
     * Изменяет данные параметра
     *
     * @param key ключ
     * @param paramEditableDto данные для редактирования параметра
     * @return отредактированный параметр
     */
    ParamDto changeParam(@NotBlank String key, @Valid ParamEditableDto paramEditableDto);
}
