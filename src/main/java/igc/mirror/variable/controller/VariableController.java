package igc.mirror.variable.controller;

import igc.mirror.variable.dto.VariableDto;
import igc.mirror.variable.service.VariableService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Переменные")
public class VariableController {

    @Autowired
    private VariableService variableService;

    @PostMapping("variable")
    @Operation(summary = "Получение списка переменных по указанным идентификаторам")
    public List<VariableDto> findVariables(@RequestBody(required = false) List<Long> variableIds) {
        return variableService.findVariables(variableIds);
    }
}
