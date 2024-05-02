package igc.mirror.appcontrol.controller;


import igc.mirror.appcontrol.dto.AppControlDto;

import igc.mirror.appcontrol.dto.AppControlEditableDto;
import igc.mirror.appcontrol.service.AppControlService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Управление сервисами(заглушка)")
public class AppControlController {

    @Autowired
    private AppControlService appControlService;

    @GetMapping("/appservice")
    @Operation(summary = "Получение списка сервисов")
    public List<AppControlDto> findAppControl(@RequestParam(required = false) String name) {
        return appControlService.findAppControl(name);
    }


    @PutMapping("/appservice/{name}")
    @Operation(summary = "Изменение значения сервиса")
    @PreAuthorize("hasAuthority('CONFIG_VALUE.CHANGE')")
    public ResponseEntity<AppControlDto> editAppControl(@PathVariable String name, @RequestBody AppControlEditableDto appControlEditableDto) {
        return ResponseEntity.ok(appControlService.changeAppControl(name, appControlEditableDto));
    }
}
