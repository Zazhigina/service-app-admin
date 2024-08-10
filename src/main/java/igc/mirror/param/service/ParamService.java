package igc.mirror.param.service;

import igc.mirror.auth.UserDetails;
import igc.mirror.exception.common.EntityNotFoundException;
import igc.mirror.param.dto.ParamDto;
import igc.mirror.param.dto.ParamEditableDto;
import igc.mirror.param.model.Param;
import igc.mirror.param.repository.ParamRepository;
import igc.mirror.utils.qfilter.DataFilter;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Validated
public class ParamService {
    static final Logger logger = LoggerFactory.getLogger(ParamService.class);
    private final UserDetails userDetails;
    private final ParamRepository paramRepository;

    @Autowired
    public ParamService(ParamRepository paramRepository,
                            UserDetails userDetails){
        this.paramRepository = paramRepository;
        this.userDetails = userDetails;
    }

    /**
     * Возвращает список параметров
     *
     * @param dataFilter набор критериев поиска
     * @param pageable настройки пэджинации и сортировки
     * @return список параметров
     */
    public Page<ParamDto> findParamsByFilters(DataFilter<?> dataFilter, Pageable pageable) {
        Page<Param> paramPage = paramRepository.findByFilters(dataFilter, pageable);

        return new PageImpl<>(paramPage.getContent().stream().map(ParamDto::fromModel).collect(Collectors.toList()),
                pageable, paramPage.getTotalElements());
    }

    /**
     * Изменяет данные параметра
     *
     * @param key ключ
     * @param paramEditableDto данные для редактирования параметра
     * @return отредактированный параметр
     */
    @Transactional
    @Validated
    public ParamDto changeParam(@NotBlank String key, @Valid ParamEditableDto paramEditableDto) {
        logger.info("Изменение параметра с ключом - {}", key);

        if(!paramRepository.checkExist(key))
            throw new EntityNotFoundException(String.format("Параметр %s не найден",key), null, ParamEditableDto.class);

        Param changeParam = new Param(key, paramEditableDto.getName(), paramEditableDto.getVal());
        changeParam.setLastUpdateUser(userDetails.getUsername());

        return ParamDto.fromModel(paramRepository.save(changeParam));
    }

    /**
     * Возвращает данные параметра по ключу
     * @param key ключ
     * @return данные параметра
     */
    public ParamDto findByKey(String key){
        return ParamDto.fromModel(paramRepository.find(key));
    }

    /**
     * Возвращает карту соответствия параметров указанным ключам
     *
     * @param keys список ключей
     * @return карта
     */
    public Map<String, ParamDto> getParamKeysAsMap(List<String> keys) {
        return paramRepository.getParamKeysAsMap(keys);
    }

    /**
     * Заполняет значение параметра "PRCAT_INTEGRATION.START_DATE"
     *
     * @param prcatIntegrationStartDate дата начала использования сервиса Справочник расценок
     * @return параметр с заполненным значением
     */
    public ParamDto fillPrcatIntegrationStartDateParam(@RequestBody LocalDateTime prcatIntegrationStartDate) {
        final String KEY = "PRCAT_INTEGRATION.START_DATE";

        ParamDto paramDto = findByKey(KEY);

        Param changeParam = new Param(KEY, paramDto.getName(), prcatIntegrationStartDate.toString());
        changeParam.setLastUpdateUser(userDetails.getUsername());

        return ParamDto.fromModel(paramRepository.save(changeParam));
    }
}