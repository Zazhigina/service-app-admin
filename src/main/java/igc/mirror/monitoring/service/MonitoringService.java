package igc.mirror.monitoring.service;

import igc.mirror.monitoring.dto.*;
import igc.mirror.monitoring.mapper.MonitoringDataMapper;
import igc.mirror.monitoring.mapper.ServiceDataMapper;
import igc.mirror.monitoring.model.MonitoringData;
import igc.mirror.monitoring.model.ServiceData;
import igc.mirror.monitoring.repository.BaseServiceRepository;
import igc.mirror.monitoring.repository.MonitoringRepository;
import jooqdata.tables.TServiceDate;
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

import java.util.*;
import java.util.function.Consumer;

import static jooqdata.Tables.T_SERVICE_DATE;
import static jooqdata.tables.TMonitoring.T_MONITORING;

@Service
@Slf4j
@RequiredArgsConstructor
public class MonitoringService {

    private final MonitoringRepository monitoringRepository;
    private final MonitoringDataMapper monitoringDataMapper;
    private final BaseServiceRepository baseServiceRepository;
    private final ServiceDataMapper serviceDataMapper;
    @Transactional
    public MonitoringDataDto createMonitoringData(MonitoringDataSaveDto monitoringDataSaveDto) {
        log.info("Запрос на сохранение MonitoringData");

        ServiceData service = monitoringRepository.findServiceByName(monitoringDataSaveDto.getName().toUpperCase());
        log.info("ServiceData найден в списке сервисов: {}", service.getName().toUpperCase());

        MonitoringData monitoringData = monitoringDataMapper.saveDtoToEntity(monitoringDataSaveDto);
        monitoringData.setServiceData(service);

        MonitoringData savedData = monitoringRepository.add(monitoringData);
        log.info("Успешно сохранено MonitoringData с ID: {}", savedData.getId());

        return monitoringDataMapper.entityToSaveDto(savedData);
    }
    @Transactional
    public MonitoringDataDto updateMonitoringData(MonitoringDataUpdateDto updateDto, Long id) {
        log.info("Запрос на обновление MonitoringData с ID={}", id);

        MonitoringData existingData = monitoringRepository.findMonitoringById(id);
        Map<Field<?>, Object> updateFields = new HashMap<>();

        updateField(updateDto.getUrl(), existingData.getUrl(), "URL", existingData::setUrl, T_MONITORING.URL, updateFields);
        updateField(updateDto.getSummary(), existingData.getSummary(), "Summary", existingData::setSummary, T_MONITORING.SUMMARY, updateFields);
        updateField(updateDto.getIsActive(), existingData.getIsActive(), "Is_Active", existingData::setIsActive, T_MONITORING.IS_ACTIVE, updateFields);

        if (updateDto.getName() != null) {
            ServiceData newService = monitoringRepository.findServiceByName(updateDto.getName().toUpperCase());
            log.info("ServiceData найден: {}", newService.getName());

            String oldServiceName = existingData.getServiceData().getName();
            if (!Objects.equals(newService.getName(), oldServiceName)) {
                log.info("Обновление Service: {} -> {}", oldServiceName, newService.getName());
                existingData.setServiceData(newService);
                updateFields.put(T_MONITORING.SERVICE_DATE_ID, newService.getId());
            }
        }

        if (updateFields.isEmpty()) {
            log.info("Данные идентичны. Обновление не требуется.");
            return monitoringDataMapper.entityToSaveDto(existingData);
        }

        MonitoringData updated = monitoringRepository.updateInDb(updateFields, id);
        updated.setServiceData(existingData.getServiceData());

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
        log.info("Диактивировано поле isActual в статистике мониторинга с ID={}", id);

        monitoringRepository.deleteMonitoringById(id);
        log.info("MonitoringData с ID={} удалён", id);
    }
    @Transactional
    public List<MonitoringDataDto> getListMonitoringData() {
        log.info("Запрос на получение всех записей MonitoringData");

        List<MonitoringData> data = monitoringRepository.findAllMonitoringData();
        log.info("Успешно получены данные");

        log.info("Найдено {} записей", data.size());

        return data.stream().map(monitoringDataMapper::entityToSaveDto).toList();
    }
    @Transactional
    public List<MonitoringCheckDto> getListInfoMonitoringData() {
        log.info("Запрос на получение списка актуальных записей статистики мониторинга сервиса");

        return monitoringRepository.findAllActualMonitoring().stream().map(monitoringDataMapper::statisticToCheckDto).toList();
    }
    @Transactional
    public List<MonitoringCheckDto> findAllMonitoringStatistic() {
        log.info("Запрос на получение списка всех записей статистики мониторинга сервиса");

        return monitoringRepository.findAllMonitoring().stream().map(monitoringDataMapper::statisticToCheckDto).toList();
    }

    @Scheduled(cron = "0 0 6 * * *", zone = "Europe/Moscow")
    @Transactional
    public void removeMonitoringStatistics() {
        log.info("Запрос на удаление статистики мониторинга");

        monitoringRepository.deleteMonitoringStatistic();
    }
    @Transactional
    public ServiceDataDto createServiceData(ServiceDataSaveDto serviceDataSaveDto) {
        serviceDataSaveDto.setName(serviceDataSaveDto.getName().toUpperCase());
        log.info("Запрос на сохранение ServiceData");

        return serviceDataMapper.entityToSaveDto(baseServiceRepository.create(serviceDataSaveDto));
    }
    @Transactional
    public ServiceDataDto updateServiceData(ServiceDataUpdateDto updatedData, Long id) {
        log.info("Запрос на обновление ServiceData");
        Map<Field<?>, Object> updateFields = new HashMap<>();

        ServiceData existingData = baseServiceRepository.findServiceById(id);

        Optional.ofNullable(updatedData.getName())
                .filter(name -> !name.equals(existingData.getName()) && !name.isEmpty())
                .ifPresent(name -> {
                    updateFields.put(TServiceDate.T_SERVICE_DATE.NAME, name.toUpperCase());
                    log.info("Поле 'name' обновлено: {} -> {}", existingData.getName(), name.toUpperCase());
                });

        Optional.ofNullable(updatedData.getDescription())
                .filter(desc -> !desc.equals(existingData.getDescription()) && !desc.isEmpty())
                .ifPresent(desc -> {
                    updateFields.put(T_SERVICE_DATE.DESCRIPTION, desc);
                    log.info("Поле 'description' обновлено: {} -> {}", existingData.getDescription(), desc);
                });

        if (updateFields.isEmpty()) {
            log.info("Данные по сервису с ID {} не были изменены.", id);
            return serviceDataMapper.entityToSaveDto(existingData);
        }

        log.info("Обновление полей сервиса ID {}: {}", id, updateFields.keySet());

        ServiceData updated = baseServiceRepository.updateInDb(updateFields, existingData.getId());
        log.info("ServiceData с ID={} успешно обновлён", id);

        return serviceDataMapper.entityToSaveDto(updated);
    }

    @Transactional
    public List<ServiceDataDto> getServiceData() {
        log.info("Запрос на получение списка ServiceData");

        List<ServiceData> allService = baseServiceRepository.findAllServiceData();
        log.info("Успешно получены данные");

        log.info("Найдено {} записей", allService.size());

        return allService.stream().map(serviceDataMapper::entityToSaveDto).toList();
    }
    @Transactional
    public void removeServiceData(Long id) {
        log.info("Запрос на удаление ServiceData");

        monitoringRepository.deactivateStatsByIdMonitoring(id);
        baseServiceRepository.deleteServiceById(id);
    }

    public Workbook generateExcel(List<MonitoringCheckDto> records) {

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Monitoring Check");

        String[] headers = {"Service Name", "Description", "URL", "Summary", "Result Check", "Create Date"};
        Row headerRow = sheet.createRow(0);
        for (int i = 0; i < headers.length; i++) {
            headerRow.createCell(i).setCellValue(headers[i]);
        }

        int rowNum = 1;
        for (MonitoringCheckDto dto : records) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(dto.getServiceName());
            row.createCell(1).setCellValue(dto.getServiceDescription());
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

}



