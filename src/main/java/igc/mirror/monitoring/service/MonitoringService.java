package igc.mirror.monitoring.service;

import igc.mirror.appcontrol.dto.AppControlDto;
import igc.mirror.appcontrol.repository.AppControlRepository;
import igc.mirror.exception.common.EntityNotFoundException;
import igc.mirror.monitoring.dto.*;
import igc.mirror.monitoring.mapper.MonitoringDataMapper;
import igc.mirror.monitoring.model.MonitoringData;
import igc.mirror.monitoring.repository.MonitoringRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jooq.Field;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Consumer;

import static jooqdata.tables.TMonitoring.T_MONITORING;

@Service
@Slf4j
@RequiredArgsConstructor
public class MonitoringService {

    private final MonitoringRepository monitoringRepository;
    private final MonitoringDataMapper monitoringDataMapper;
    private final AppControlRepository appControlRepository;

    @Transactional
    public MonitoringDataDto createMonitoringData(MonitoringDataSaveDto monitoringDataSaveDto) {
        log.info("Запрос на сохранение MonitoringData");

        List<String> serviceNameList = getServiceData();

        String inputServiceName = monitoringDataSaveDto.getServiceName();

        Optional<String> matchedServiceName = serviceNameList.stream()
                .filter(s -> s.equalsIgnoreCase(inputServiceName))
                .findFirst();

        if (matchedServiceName.isPresent()) {
            log.info("Имя сервиса найден в списке сервисов");

            monitoringDataSaveDto.setServiceName(matchedServiceName.get());

            MonitoringData savedData = monitoringRepository.add(monitoringDataSaveDto);
            log.info("Успешно сохранено MonitoringData с ID: {}", savedData.getId());

            return monitoringDataMapper.entityToSaveDto(savedData);
        } else {
            throw new EntityNotFoundException(
                    String.format("Ошибка: сервиса с именем %s не существует!", monitoringDataSaveDto.getServiceName()),
                    null, MonitoringData.class
            );
        }
    }

    @Transactional
    public MonitoringDataDto updateMonitoringData(MonitoringDataUpdateDto updateDto, Long id) {
        log.info("Запрос на обновление MonitoringData с ID={}", id);

        MonitoringData existingData = monitoringRepository.findMonitoringById(id);
        Map<Field<?>, Object> updateFields = new HashMap<>();

        updateField(updateDto.getUrl(), existingData.getUrl(), "URL", existingData::setUrl, T_MONITORING.URL, updateFields);
        updateField(updateDto.getSummary(), existingData.getSummary(), "Summary", existingData::setSummary, T_MONITORING.SUMMARY, updateFields);
        updateField(updateDto.getIsActive(), existingData.getIsActive(), "Is_Active", existingData::setIsActive, T_MONITORING.IS_ACTIVE, updateFields);

        if (updateDto.getServiceName() != null) {
            List<String> serviceNameList = getServiceData();

            String inputServiceName = updateDto.getServiceName();

            Optional<String> matchedServiceName = serviceNameList.stream()
                    .filter(s -> s.equalsIgnoreCase(inputServiceName))
                    .findFirst();

            if (matchedServiceName.isPresent()) {
                String newService = matchedServiceName.get().toUpperCase();

                log.info("ServiceData найден: {}", newService);

                String oldServiceName = existingData.getServiceName();

                updateField(matchedServiceName.get(), oldServiceName, "service_name", existingData::setServiceName, T_MONITORING.SERVICE_NAME, updateFields);
            }

        }

        if (updateFields.isEmpty()) {
            log.info("Данные идентичны. Обновление не требуется.");
            return monitoringDataMapper.entityToSaveDto(existingData);
        }

        MonitoringData updated = monitoringRepository.updateInDb(updateFields, id);

        log.info("MonitoringData с ID={} успешно обновлён", id);
        return monitoringDataMapper.entityToSaveDto(updated);
    }

    private <T> void updateField(
            T newValue,
            T oldValue,
            String fieldName,
            Consumer<T> setter,
            Field<?> dbField,
            Map<Field<?>, Object> fields
    ) {
        if (newValue != null && !Objects.equals(newValue, oldValue)) {
            log.info("Обновление {}: {} -> {}", fieldName, oldValue, newValue);
            setter.accept(newValue);
            fields.put(dbField, newValue);
        }
    }

    @Transactional
    public void removeMonitoringData(Long id) {
        log.info("Запрос на удаление MonitoringData");

        monitoringRepository.deactivateStatsByIdMonitoring(id);
        log.info("Активировано поле deleted в статистике мониторинга с ID={}", id);

        monitoringRepository.deleteMonitoringById(id);
        log.info("MonitoringData с ID={} удалён", id);
    }

    @Transactional
    public List<MonitoringData> getListMonitoringData() {
        log.info("Запрос на получение всех записей MonitoringData");

        List<MonitoringData> data = monitoringRepository.findAllMonitoringData();
        log.info("Успешно получены данные");

        log.info("Найдено {} записей", data.size());

        return data;
    }

    @Transactional
    public List<MonitoringStatisticDto> getListInfoMonitoringData() {
        log.info("Запрос на получение списка актуальных записей статистики мониторинга сервиса");

        return monitoringRepository.findAllActualMonitoring().stream().map(monitoringDataMapper::statisticToCheckDto).toList();
    }

    @Transactional
    public List<MonitoringStatisticDto> findAllMonitoringStatistic() {
        log.info("Запрос на получение списка всех записей статистики мониторинга сервиса");

        return monitoringRepository.findAllMonitoring().stream().map(monitoringDataMapper::statisticToCheckDto).toList();
    }

    @Scheduled(cron = "0 0 6 * * *", zone = "Europe/Moscow")
    @Transactional
    public void removeMonitoringStatistics() {
        log.info("Запрос на удаление статистики мониторинга");

        monitoringRepository.deleteMonitoringStatistic();
    }

    @Scheduled(cron = "0 0 * * * *", zone = "Europe/Moscow")
    @Transactional
    public void removeOldMonitoringStatistics() {
        LocalDateTime twoWeeksAgo = LocalDateTime.now().minusWeeks(2);
        int deletedCount = monitoringRepository.deleteOldMonitoringStatistics(twoWeeksAgo);
        log.info("Удалено {} записей статистики мониторинга старше {}", deletedCount, twoWeeksAgo);
    }

    @Transactional
    public List<String> getServiceData() {
        log.info("Запрос на получение списка сервисов");

        List<AppControlDto> allService = appControlRepository.findAppControl("");
        log.info("Успешно получены данные");

        log.info("Найдено {} записей", allService.size());

        return allService.stream().map(service -> service.getName().substring(3).toUpperCase()).toList();
    }

    @Transactional
    public Workbook generateExcel(List<MonitoringStatisticDto> records) {

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Monitoring Check");

        String[] headers = {"Service Name", "Description", "URL", "Summary", "Result Check", "Create Date"};
        Row headerRow = sheet.createRow(0);
        for (int i = 0; i < headers.length; i++) {
            headerRow.createCell(i).setCellValue(headers[i]);
        }

        int rowNum = 1;
        for (MonitoringStatisticDto dto : records) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(dto.getServiceName());
            row.createCell(2).setCellValue(dto.getUrl());
            row.createCell(3).setCellValue(dto.getSummary());
            row.createCell(4).setCellValue(dto.getResultCheck());
            row.createCell(5).setCellValue(dto.getCreateDate() != null ? dto.getCreateDate().toString() : "");
        }

        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
        }

        return workbook;
    }

    @Transactional
    public void setAllActual(boolean active) {
        monitoringRepository.setAllActual(active);
    }
}
