package igc.mirror.appcontrol.service;

import igc.mirror.auth.UserDetails;
import igc.mirror.appcontrol.dto.AppControlDto;
import igc.mirror.appcontrol.dto.AppControlEditableDto;
import igc.mirror.appcontrol.repository.AppControlRepository;
import igc.mirror.exception.common.EntityNotFoundException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Service
@Validated
public class AppControlService {
    static final Logger logger = LoggerFactory.getLogger(AppControlService.class);
    private final UserDetails userDetails;
    private final AppControlRepository appControlRepository;
    @Autowired
    public AppControlService(AppControlRepository appControlRepository,
                             UserDetails userDetails) {
        this.appControlRepository = appControlRepository;
        this.userDetails = userDetails;
    }

    /**
     * Находит сервисы по названию сервиса
     *
     * @param name название сервиса
     * @return список сервисов
     */
    public List<AppControlDto> findAppControl(String name) {
        return appControlRepository.findAppControl(name);
    }


    /**
     * Изменяет данные сервиса
     *
     * @param name название сервиса
     * @param appControlEditableDto данные для редактирования сервиса
     * @return отредактированный сервис
     */
    @Transactional
    @Validated
    public AppControlDto changeAppControl(@NotBlank String name, @Valid AppControlEditableDto appControlEditableDto) {
        logger.info("Изменение сервиса с названием - {}", name);

        if(!appControlRepository.checkExist(name))
            throw new EntityNotFoundException(String.format("Сервис %s не найден",name), null, AppControlEditableDto.class);

        AppControlDto changeAppControl = new AppControlDto(name, appControlEditableDto.getDescription(), appControlEditableDto.getEnabled());
        changeAppControl.setLastUpdateUser(userDetails.getUsername());

        return appControlRepository.save(changeAppControl);
    }
}
