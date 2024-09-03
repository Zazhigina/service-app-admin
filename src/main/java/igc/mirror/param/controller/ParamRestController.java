package igc.mirror.param.controller;

import igc.mirror.param.dto.ParamDto;
import igc.mirror.param.dto.ParamEditableDto;
import igc.mirror.param.dto.ProfessionalEmailDto;
import igc.mirror.param.service.ParamService;
import igc.mirror.param.service.RemoteParamService;
import igc.mirror.utils.qfilter.DataFilter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("param")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Ведение параметров")
public class ParamRestController {
    private final ParamService paramService;
    private final RemoteParamService remoteParamService;

    public ParamRestController(ParamService paramService, RemoteParamService remoteParamService) {
        this.paramService = paramService;
        this.remoteParamService = remoteParamService;
    }

    @PostMapping("find")
    @Operation(summary = "Поиск параметров, поддерживается фильтрация/сортировка/пагинация")
    @PreAuthorize("hasAuthority('CONFIG_VALUE.READ')")
    public Page<ParamDto> findParamsByFilters(@RequestBody(required = false) DataFilter<?> filter, @ParameterObject Pageable pageable) {
        return paramService.findParamsByFilters(filter, pageable);
    }

    @PutMapping("{key}")
    @Operation(summary = "Изменение значения параметра")
    @PreAuthorize("hasAuthority('CONFIG_VALUE.CHANGE')")
    public ResponseEntity<ParamDto> editParam(@PathVariable String key, @RequestBody ParamEditableDto paramEditableDto) {
        return ResponseEntity.ok(paramService.changeParam(key, paramEditableDto));
    }

    @GetMapping("/{key}")
    @Operation(summary = "Поиск параметра по ключу")
    @PreAuthorize("hasAuthority('CONFIG_VALUE.READ')")
    public ResponseEntity<ParamDto> findByKey(@PathVariable String key) {
        return ResponseEntity.ok(paramService.findByKey(key));
    }

    @GetMapping("/professional-email")
    @Operation(summary = "Почтовые ящики для Профессионал 4.0")
    public ResponseEntity<ProfessionalEmailDto> findProfessionalEmail() {
        return ResponseEntity.ok(paramService.findProfessionalEmails());
    }

    @PostMapping("/map")
    @Operation(summary = "Карта соответствия параметров по ключам")
    public Map<String, ParamDto> getParamKeysAsMap(@RequestBody List<String> keys) {
        return paramService.getParamKeysAsMap(keys);
    }

    @DeleteMapping("/cache-remote")
    @Operation(summary = "Очистка кеша параметров удаленных сервисов")
    @PreAuthorize("hasAuthority('CONFIG_VALUE.CHANGE')")
    public ResponseEntity<String> clearRemoteServiceParamCache() {
        remoteParamService.clearMaParameterCache();
        String message = "Задание на очистку кеша параметров передано в приложение ma";
        remoteParamService.clearEpParameterCache();
        message = message + ", ep";
        remoteParamService.clearChatParameterCache();
        message = message + ", chat";
        return ResponseEntity.ok(message);
    }

    @PutMapping("prcat-integration-start-date")
    @Operation(summary = "Заполнение значения параметра PRCAT_INTEGRATION.START_DATE")
    public ParamDto fillPrcatIntegrationStartDateParam(@RequestBody LocalDateTime prcatIntegrationStartDate) {
        return paramService.fillPrcatIntegrationStartDateParam(prcatIntegrationStartDate);
    }
}
