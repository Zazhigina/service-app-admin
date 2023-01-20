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
    Page<ParamDto> findParamsByFilters(DataFilter<?> dataFilter, Pageable pageable);
    ParamDto addNewParam(@Valid ParamCreationDto paramCreationDto);
    ParamDto changeParam(@NotBlank(message = "key is empty") String key, @Valid ParamEditableDto paramEditableDto);
    void removeParam(@NotBlank(message = "key is empty") String key);
    void removeParams(@Valid ParamRemovalListDto paramRemovalListDto);
}
