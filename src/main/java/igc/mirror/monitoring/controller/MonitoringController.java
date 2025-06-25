package igc.mirror.monitoring.controller;

import igc.mirror.exception.handler.SuccessInfo;
import igc.mirror.monitoring.dto.*;
import igc.mirror.monitoring.model.MonitoringData;
import igc.mirror.monitoring.service.MonitoringService;
import igc.mirror.param.dto.ParamEditableDto;
import igc.mirror.param.service.ParamService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("monitoring")
@SecurityRequirement(name = "bearerAuth")
@RequiredArgsConstructor
@Validated
@Slf4j
@Tag(name = "Управление URL для мониторинга")
public class MonitoringController {
    private final MonitoringService monitoringService;
    private final ParamService paramService;

    @GetMapping()
    @CrossOrigin(origins = {"http://localhost:3000"})
    @Operation(summary = "Получить список сервисов для мониторинга")
    public List<MonitoringData> getMonitoringData() {
        return monitoringService.getListMonitoringData();
    }

    @GetMapping({"/actual"})
    @CrossOrigin(origins = {"http://localhost:3000"})
    @Operation(summary = "Получить все актуальные данные по статистике сервисов для мониторинга ")
    public List<MonitoringStatisticDto> getInfoMonitoringData() {
        return monitoringService.getListInfoMonitoringData();
    }

    @PostMapping({"/create"})
    @CrossOrigin(origins = {"http://localhost:3000"})
    @Operation(summary = "Создание новых данных для мониторинга")
    public MonitoringDataDto createMonitoringData(@RequestBody @Valid MonitoringDataSaveDto monitoringDataSaveDto) {
        return monitoringService.createMonitoringData(monitoringDataSaveDto);
    }

    @PutMapping({"/{id}"})
    @CrossOrigin(origins = {"http://localhost:3000"})
    @Operation(summary = "Обновление данных мониторинга по id")
    public MonitoringDataDto updateMonitoringData(@RequestBody @Valid MonitoringDataUpdateDto monitoringDataUpdateDto, @PathVariable @NotNull @Positive Long id) {
        return monitoringService.updateMonitoringData(monitoringDataUpdateDto, id);
    }

    @PostMapping({"/set-actual"})
    @CrossOrigin(origins = {"http://localhost:3000"})
    @Operation(summary = "Массовое обновление актуальности всех записей Monitoring")
    public void setAllActual(@RequestBody @Valid MonitoringBatchUpdateRequest request) {
        monitoringService.setAllActual(request.isActive());
    }

    @DeleteMapping({"/{id}"})
    @CrossOrigin(origins = {"http://localhost:3000"})
    @Operation(summary = "Удаление данных для мониторинга")
    public SuccessInfo removeMonitoringData(@PathVariable @NotNull @Positive Long id) {
        monitoringService.removeMonitoringData(id);
        return new SuccessInfo("Операция выполнена");
    }

    @PatchMapping("/start")
    @CrossOrigin(origins = {"http://localhost:3000"})
    @ResponseStatus(HttpStatus.OK)
    public void start() {
        log.info("PATCH запрос на включение проверки заявок");
        paramService.changeParam("HEALTH_MONITORING_ON", new ParamEditableDto("Мониторинг сервисов 'Включен'", "TRUE"));
    }

    @PatchMapping("/stop")
    @CrossOrigin(origins = {"http://localhost:3000"})
    @ResponseStatus(HttpStatus.OK)
    public void stop() {
        log.info("PATCH запрос на выключение проверки заявок");
        paramService.changeParam("HEALTH_MONITORING_ON", new ParamEditableDto("Мониторинг сервисов 'Выключен'", "FALSE"));
    }

    @GetMapping("/export")
    @CrossOrigin(origins = {"http://localhost:3000"})
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ByteArrayResource> exportMonitoringData() {
        log.info("GET запрос на формирование файлов из мониторинга");

        List<MonitoringStatisticDto> records = monitoringService.findAllMonitoringStatistic();
        try (Workbook workbook = monitoringService.generateExcel(records);
             ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            workbook.write(out);
            ByteArrayResource resource = new ByteArrayResource(out.toByteArray());

            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=monitoring_statistics.xlsx");

            return ResponseEntity.ok()
                    .headers(headers)
                    .contentLength(resource.contentLength())
                    .contentType(MediaType.parseMediaType(
                            "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                    .body(resource);

        } catch (IOException e) {
            throw new RuntimeException("Ошибка при создании Excel-файла", e);
        }
    }

    @GetMapping("/service")
    @Operation(summary = "Получить список сервисов")
    @CrossOrigin(origins = {"http://localhost:3000"})
    public List<String> getServiceData() {
        return monitoringService.getServiceData();
    }

}
