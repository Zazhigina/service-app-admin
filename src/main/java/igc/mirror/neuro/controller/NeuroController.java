package igc.mirror.neuro.controller;

import igc.mirror.neuro.dto.ElasticSearchOperations;
import igc.mirror.neuro.dto.NeuronetInfoDto;
import igc.mirror.neuro.dto.SystemConfigDto;
import igc.mirror.neuro.ref.ConfigType;
import igc.mirror.neuro.service.NeuroService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Администрирование поиска")
public class NeuroController {

    @Autowired
    private NeuroService neuroService;

    @GetMapping("/system-config")
    @Operation(summary = "Получить конфигурацию")
    @PreAuthorize("hasAuthority('CONFIG_VALUE.READ')")
    public List<SystemConfigDto> getSystemConfig(@RequestParam(name = "configType", required = false, defaultValue = "ACTIVE") ConfigType configType) {
        return neuroService.loadSystemConfig(configType);
    }

    @PutMapping("/system-config")
    @Operation(summary = "Добавить конфигурацию")
    @PreAuthorize("hasAuthority('CONFIG_VALUE.CHANGE')")
    public ElasticSearchOperations addSystemConfig(@RequestBody SystemConfigDto systemConfigDto) {
        return neuroService.addSystemConfig(systemConfigDto);
    }

    @DeleteMapping("/system-config")
    @Operation(summary = "Удалить конфигурацию")
    @PreAuthorize("hasAuthority('CONFIG_VALUE.CHANGE')")
    public ElasticSearchOperations deleteSystemConfig(@RequestParam(name = "version") String version) {
        return neuroService.deleteSystemConfig(version);
    }

    @PostMapping("/system-config/activate")
    @Operation(summary = "Активировать конфигурацию")
    @PreAuthorize("hasAuthority('CONFIG_VALUE.CHANGE')")
    public ElasticSearchOperations activateSystemConfig(@RequestParam(name = "version") String version) {
        return neuroService.activateSystemConfig(version);
    }

    @GetMapping("/neuronet")
    @Operation(summary = "Получить список нейросетей")
    @PreAuthorize("hasAuthority('CONFIG_VALUE.READ')")
    public List<NeuronetInfoDto> getNeuronets() {
        return neuroService.loadListNeuronets();
    }

    @DeleteMapping("/index")
    @Operation(summary = "Удалить индексы для заданной версии конфигурации")
    @PreAuthorize("hasAuthority('CONFIG_VALUE.CHANGE')")
    public List<ElasticSearchOperations> deleteIndexes(@RequestParam(name = "version") String version) {
        return neuroService.deleteEsIndexes(version);
    }

    @PostMapping("/build-vectors")
    @Operation(summary = "Перегенерировать вектора в ElasticSearch")
    @PreAuthorize("hasAuthority('CONFIG_VALUE.CHANGE')")
    public ElasticSearchOperations buildEsVectors(@RequestParam(name = "version") String version) {
        return neuroService.buildVectors(version);
    }
}
