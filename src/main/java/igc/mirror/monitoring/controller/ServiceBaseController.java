package igc.mirror.monitoring.controller;

import igc.mirror.exception.handler.SuccessInfo;
import igc.mirror.monitoring.dto.ServiceDataDto;
import igc.mirror.monitoring.dto.ServiceDataSaveDto;
import igc.mirror.monitoring.dto.ServiceDataUpdateDto;
import igc.mirror.monitoring.service.MonitoringService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("services")
@SecurityRequirement(name = "bearerAuth")
@RequiredArgsConstructor
@Validated
@Tag(name = "Управление сервисами")
public class ServiceBaseController {
    private final MonitoringService monitoringService;

    @GetMapping
    @Operation(summary = "Получить список сервисов")
    //@CrossOrigin(origins = {"http://localhost:3000"})
    public List<ServiceDataDto> getServiceData() {
        return monitoringService.getServiceData();
    }

    @PostMapping
    @Operation(summary = "Новая запись сервиса")
    public ServiceDataDto createServiceData(@RequestBody @Valid ServiceDataSaveDto serviceDataSaveDto) {
        return monitoringService.createServiceData(serviceDataSaveDto);
    }

    @PutMapping({"/{id}"})
    @Operation(summary = "Обновление данных сервиса по id")
    public ServiceDataDto updateServiceData(@RequestBody @Valid ServiceDataUpdateDto serviceDataUpdateDto, @PathVariable @NotNull @Positive Long id) {
        return monitoringService.updateServiceData(serviceDataUpdateDto, id);
    }

    @DeleteMapping({"/{id}"})
    @Operation(summary = "Удаление сервиса")
    public SuccessInfo removeMonitoringDate(@PathVariable @NotNull @Positive Long id) {
        monitoringService.removeServiceData(id);
        return new SuccessInfo("Операция выполнена");
    }

}
