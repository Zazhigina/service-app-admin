package igc.mirror.service;

import igc.mirror.dto.ParamCreationDto;
import igc.mirror.dto.ParamDto;
import igc.mirror.dto.ParamEditableDto;
import igc.mirror.dto.ParamRemovalListDto;
import igc.mirror.utils.qfilter.DataFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

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
     * Добавляет новый параметр
     *
     * @param paramCreationDto данные для добавления нового параметра
     * @return Данные нового параметра
     */
    ParamDto addNewParam(@Valid ParamCreationDto paramCreationDto);

    /**
     * Изменяет данные параметра
     *
     * @param key ключ
     * @param paramEditableDto данные для редактирования параметра
     * @return отредактированный параметр
     */
    ParamDto changeParam(@NotBlank String key, @Valid ParamEditableDto paramEditableDto);

    /**
     * Удаляет параметр по ключу
     *
     * @param key ключ параметра
     */
    void removeParam(@NotBlank String key);

    /**
     * Удаляет параметры по списку ключей
     *
     * @param paramRemovalListDto список ключей
     */
    void removeParams(@Valid ParamRemovalListDto paramRemovalListDto);
}
