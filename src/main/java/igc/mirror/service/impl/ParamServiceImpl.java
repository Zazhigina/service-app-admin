package igc.mirror.service.impl;

import igc.mirror.dto.ParamCreationDto;
import igc.mirror.dto.ParamDto;
import igc.mirror.dto.ParamEditableDto;
import igc.mirror.dto.ParamRemovalListDto;
import igc.mirror.exception.common.EntityNotFoundException;
import igc.mirror.exception.common.IllegalEntityStateException;
import igc.mirror.model.Param;
import igc.mirror.repository.ParamRepository;
import igc.mirror.service.ParamService;
import igc.mirror.utils.UserHelper;
import igc.mirror.utils.qfilter.DataFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
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
    public ParamDto addNewParam(@Valid ParamCreationDto paramCreationDto) {
        if(paramRepository.checkExist(paramCreationDto.getKey()))
            throw new IllegalEntityStateException(String.format("Параметр %s уже существует в системе", paramCreationDto.getKey()), null, ParamCreationDto.class);

        Param newParam = new Param(paramCreationDto.getKey(), paramCreationDto.getName(), paramCreationDto.getVal());
        newParam.setCreateUser(userHelper.getUsername().orElse(null));

        return ParamDto.fromModel(paramRepository.save(newParam));
    }

    @Override
    @Validated
    public ParamDto changeParam(@NotBlank String key, @Valid ParamEditableDto paramEditableDto) {
        if(!paramRepository.checkExist(key))
            throw new EntityNotFoundException(String.format("Параметр %s не найден",key), null, ParamEditableDto.class);

        Param changeParam = new Param(key, paramEditableDto.getName(), paramEditableDto.getVal());
        changeParam.setLastUpdateUser(userHelper.getUsername().orElse(null));

        return ParamDto.fromModel(paramRepository.save(changeParam));
    }

    @Override
    @Validated
    public void removeParam(@NotBlank String key) {
        if(!paramRepository.checkExist(key))
            throw new EntityNotFoundException(String.format("Параметр %s не найден", key) , null, null);

        paramRepository.delete(key);
    }

    @Override
    @Validated
    public void removeParams(@Valid ParamRemovalListDto paramRemovalListDto) {
        if(paramRemovalListDto.getParamKeys().size() == 0)
            throw new IllegalEntityStateException("Параметры должны быть заполнены", null, null);

        paramRepository.deleteList(paramRemovalListDto.getParamKeys());
    }

}
