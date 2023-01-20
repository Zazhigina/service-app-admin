package igc.mirror.service.impl;

import igc.mirror.dto.ParamCreationDto;
import igc.mirror.dto.ParamDto;
import igc.mirror.dto.ParamEditableDto;
import igc.mirror.dto.ParamRemovalListDto;
import igc.mirror.exception.ParamAlreadyExistException;
import igc.mirror.exception.ParamNotFoundException;
import igc.mirror.model.Param;
import igc.mirror.repository.ParamRepository;
import igc.mirror.service.ParamService;
import igc.mirror.service.UserService;
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

    private UserService userService;
    private ParamRepository paramRepository;

    @Autowired
    public ParamServiceImpl(ParamRepository paramRepository, UserService userService){
        this.paramRepository = paramRepository;
        this.userService = userService;
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
            throw new ParamAlreadyExistException();

        Param newParam = new Param(paramCreationDto.getKey(), paramCreationDto.getName(), paramCreationDto.getVal());
        newParam.setCreateUser(userService.getUsername());

        return ParamDto.fromModel(paramRepository.save(newParam));
    }

    @Override
    @Validated
    public ParamDto changeParam(@NotBlank(message = "key is empty") String key, @Valid ParamEditableDto paramEditableDto) {
        if(!paramRepository.checkExist(key))
            throw new ParamNotFoundException();

        Param changeParam = new Param(key, paramEditableDto.getName(), paramEditableDto.getVal());
        changeParam.setLastUpdateUser(userService.getUsername());

        return ParamDto.fromModel(paramRepository.save(changeParam));
    }

    @Override
    @Validated
    public void removeParam(@NotBlank(message = "key is empty") String key) {
        if(!paramRepository.checkExist(key))
            throw new ParamNotFoundException();

        paramRepository.delete(key);
    }

    @Override
    @Validated
    public void removeParams(@Valid ParamRemovalListDto paramRemovalListDto) {
        if(paramRemovalListDto.getParamKeys().size() == 0)
            throw new ParamNotFoundException("Параметры должны быть заполнены");

        paramRepository.deleteList(paramRemovalListDto.getParamKeys());
    }

}
