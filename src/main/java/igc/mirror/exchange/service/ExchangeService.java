package igc.mirror.exchange.service;

import igc.mirror.auth.UserDetails;
import igc.mirror.exception.common.EntityNotSavedException;
import igc.mirror.exchange.dto.ExternalSourceDto;
import igc.mirror.exchange.model.ExternalSource;
import igc.mirror.exchange.repository.ExternalSourceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ExchangeService {
    private final ExternalSourceRepository externalSourceRepository;
    private final UserDetails userDetails;

    /**
     * Добавить запись о системе в справочник
     * @param externalSourceDto DTO для сохранения данных
     * @return Идентификатор сохраненной записи
     */
    public Long createExternalSource(ExternalSourceDto externalSourceDto) {
        ExternalSource model = externalSourceDto.toModel();
        model.setCreateUser(userDetails.getUsername());

        return externalSourceRepository.createExternalSource(model);
    }

    /**
     * Изменить запись в справочнике систем
     * @param externalSourceDto Запись для изменения
     */
    public void saveExternalSource(ExternalSourceDto externalSourceDto) {
        if (externalSourceDto.id() != null) {
            ExternalSource model = externalSourceDto.toModel();
            model.setLastUpdateUser(userDetails.getUsername());

            externalSourceRepository.saveExternalSource(model);
        } else {
            throw new EntityNotSavedException("Возникли ошибки при сохранении данных. Идентификатор записи не задан", null, ExternalSourceDto.class);
        }
    }

    /**
     * Пометить запись для удаления
     * @param id Идентификатор записи из справочника систем
     */
    public void deleteExternalSource(Long id) {
        externalSourceRepository.markDeletedById(id, userDetails.getUsername());
    }

    /**
     * Найти запись по ее идентификатору
     * @param id Идентификатор записи из справочника систем
     * @return Данные
     */
    public ExternalSourceDto findExternalSourceById(Long id) {
        return Optional.ofNullable(externalSourceRepository.findById(id))
                .map(ExternalSourceDto::new)
                .orElse(null);
    }

    /**
     * Список всех записей из справочника систем
     * @return Список
     */
    public List<ExternalSourceDto> getExternalSources() {
        return externalSourceRepository.getAllExternalSources()
                .stream()
                .map(ExternalSourceDto::new)
                .toList();
    }
}
