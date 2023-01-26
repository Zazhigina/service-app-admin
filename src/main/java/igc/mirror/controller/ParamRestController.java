package igc.mirror.controller;

import igc.mirror.dto.ParamDto;
import igc.mirror.dto.ParamEditableDto;
import igc.mirror.dto.ParamRemovalListDto;
import igc.mirror.service.ParamService;
import igc.mirror.utils.qfilter.DataFilter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("param")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Ведение параметров")
public class ParamRestController {
    private ParamService paramService;

    @Autowired
    public ParamRestController(ParamService paramService){
        this.paramService = paramService;
    }

    @PostMapping("find")
    @Operation(summary = "Поиск параметров, поддерживается фильтрация/сортировка/пагинация")
    @PreAuthorize("hasRole('CONFIG_VALUE.READ')")
    public Page<ParamDto> findParamsByFilters(@RequestBody(required = false) DataFilter<?> filter, @ParameterObject Pageable pageable){
        return paramService.findParamsByFilters(filter, pageable);
    }

//    @PostMapping("add")
//    @Operation(summary = "Создание нового параметра")
//    public ResponseEntity<ParamDto> addNewParam(@RequestBody ParamCreationDto paramCreationDto){
//        return ResponseEntity.ok(paramService.addNewParam(paramCreationDto));
//    }

    @PutMapping("{key}")
    @Operation(summary = "Изменение значения параметра")
    @PreAuthorize("hasRole('CONFIG_VALUE.CHANGE')")
    public ResponseEntity<ParamDto> editParam(@PathVariable String key, @RequestBody ParamEditableDto paramEditableDto){
        return ResponseEntity.ok(paramService.changeParam(key, paramEditableDto));
    }

    @DeleteMapping("{key}")
    @Operation(summary = "Удаление параметра")
    @PreAuthorize("hasRole('CONFIG_VALUE.CHANGE')")
    public ResponseEntity<?> removeParam(@PathVariable String key){
        paramService.removeParam(key);
        return ResponseEntity.ok(true);
    }

    @PutMapping("removeList")
    @Operation(summary = "Удаление параметров")
    @PreAuthorize("hasRole('CONFIG_VALUE.CHANGE')")
    public ResponseEntity<?> removeParams(@RequestBody ParamRemovalListDto paramRemovalListDto){
        paramService.removeParams(paramRemovalListDto);
        return ResponseEntity.ok(true);
    }
}
