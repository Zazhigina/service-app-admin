package igc.mirror.service.impl;

import igc.mirror.dto.ParamDto;
import igc.mirror.dto.ParamEditableDto;
import igc.mirror.exception.common.EntityNotFoundException;
import igc.mirror.model.Param;
import igc.mirror.repository.ParamRepository;
import igc.mirror.service.ParamService;
import igc.mirror.utils.UserHelper;
import igc.mirror.utils.qfilter.DataFilter;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.stream.Collectors;

@Service
@Validated
public class ParamServiceImpl implements ParamService {

    private final UserHelper userHelper;
    private final ParamRepository paramRepository;

    @Autowired
    public ParamServiceImpl(ParamRepository paramRepository,
                            UserHelper userHelper){
        this.paramRepository = paramRepository;
        this.userHelper = userHelper;
    }

    @Override
    public Page<ParamDto> findParamsByFilters(DataFilter<?> dataFilter, Pageable pageable) {
        Page<Param> paramPage = paramRepository.findByFilters(dataFilter, pageable);

        return new PageImpl<>(paramPage.getContent().stream().map(paramModel -> ParamDto.fromModel(paramModel)).collect(Collectors.toList()),
                pageable, paramPage.getTotalElements());
    }

    @Override
    @Validated
    public ParamDto changeParam(@NotBlank String key, @Valid ParamEditableDto paramEditableDto) {
        if(!paramRepository.checkExist(key))
            throw new EntityNotFoundException(String.format("Параметр %s не найден",key), null, ParamEditableDto.class);

        Param changeParam = new Param(key, paramEditableDto.getName(), paramEditableDto.getVal());
        changeParam.setLastUpdateUser(userHelper.getUsername());

        return ParamDto.fromModel(paramRepository.save(changeParam));
    }

}
