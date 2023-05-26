package igc.mirror.controller;

import igc.mirror.dto.ParamDto;
import igc.mirror.dto.ParamEditableDto;
import igc.mirror.service.ParamService;
import igc.mirror.utils.qfilter.DataFilter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springdoc.core.annotations.ParameterObject;
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
    @PreAuthorize("hasAuthority('CONFIG_VALUE.READ')")
    public Page<ParamDto> findParamsByFilters(@RequestBody(required = false) DataFilter<?> filter, @ParameterObject Pageable pageable){
        return paramService.findParamsByFilters(filter, pageable);
    }

    @PutMapping("{key}")
    @Operation(summary = "Изменение значения параметра")
    @PreAuthorize("hasAuthority('CONFIG_VALUE.CHANGE')")
    public ResponseEntity<ParamDto> editParam(@PathVariable String key, @RequestBody ParamEditableDto paramEditableDto){
        return ResponseEntity.ok(paramService.changeParam(key, paramEditableDto));
    }

    @GetMapping("/{key}")
    @Operation(summary = "Поиск параметра по ключу")
    @PreAuthorize("hasAuthority('CONFIG_VALUE.READ')")
    public ResponseEntity<ParamDto> findByKey(@PathVariable String key){
        return ResponseEntity.ok(paramService.findByKey(key));
    }

}
