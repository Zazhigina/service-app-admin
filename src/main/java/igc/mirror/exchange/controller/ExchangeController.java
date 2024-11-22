package igc.mirror.exchange.controller;

import igc.mirror.exception.handler.SuccessInfo;
import igc.mirror.exchange.dto.ExternalSourceDto;
import igc.mirror.exchange.model.ProcedureData;
import igc.mirror.exchange.service.ExchangeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

@RestController
@RequestMapping("exchange")
@Tag(name = "Обмен с внешними сервисами")
@SecurityRequirement(name = "bearerAuth")
@RequiredArgsConstructor
public class ExchangeController {
    private final ExchangeService exchangeService;

    @Operation(summary = "Создать запись в справочнике систем")
    @PostMapping("ref/external-source")
    public Long createExternalSource(@RequestBody @Valid ExternalSourceDto externalSourceDto) {
        return exchangeService.createExternalSource(externalSourceDto);
    }

    @Operation(summary = "Изменить запись в справочнике систем")
    @PutMapping("ref/external-source")
    public ResponseEntity<SuccessInfo> changeExternalSource(@RequestBody @Valid ExternalSourceDto externalSourceDto) {
        exchangeService.changeExternalSource(externalSourceDto);
        return new ResponseEntity<>(new SuccessInfo("Объект сохранен"), HttpStatus.OK);
    }

    @Operation(summary = "Пометить на удаление запись в справочнике систем")
    @DeleteMapping("ref/external-source/{id}")
    public ResponseEntity<SuccessInfo> deleteExternalSource(@PathVariable Long id) {
        exchangeService.deleteExternalSource(id);
        return new ResponseEntity<>(new SuccessInfo("Объект помечен на удаление"), HttpStatus.OK);
    }

    @Operation(summary = "Получить запись из справочника систем по id")
    @GetMapping("ref/external-source/{id}")
    public ExternalSourceDto findExternalSourceById(@PathVariable Long id) {
        return exchangeService.findExternalSourceById(id);
    }

    @Operation(summary = "Получить все записи из справочника систем")
    @GetMapping("ref/external-source")
    public List<ExternalSourceDto> getExternalSources() {
        return exchangeService.getExternalSources();
    }

    @Operation(summary = "Загрузка закупочных процедур из собственного опыта")
    @PostMapping("{source}/procedure")
    public ResponseEntity<SuccessInfo> loadPurchaseProcedureByFile(@PathVariable String source, @RequestParam("file") MultipartFile file) {
        exchangeService.loadPurchaseProcedureByFile(source, file);
        return new ResponseEntity<>(new SuccessInfo("Данные загружены"), HttpStatus.OK);
    }

    @Operation(summary = "Тестовый метод: Загрузка закупочных процедур из собственного опыта - только разбор файла")
    @PostMapping("{source}/procedure/test")
    public ProcedureData loadPurchaseProcedureByFileTest(@PathVariable String source, @RequestParam("file") MultipartFile file) {
        return exchangeService.loadPurchaseProcedureByFileTest(source, file);
    }

    @Operation(summary = "Получить шаблон для загрузки закупочных процедур")
    @GetMapping("procedure/template")
    public ResponseEntity<Resource> getPurchaseProcedureTemplate() throws IOException {
        Resource resource = exchangeService.getPurchaseProcedureTemplate();

        HttpHeaders httpHeaders = new HttpHeaders();
        ContentDisposition contentDisposition = ContentDisposition
                .attachment()
                .filename(resource.getFilename(), StandardCharsets.UTF_8)
                .build();
        httpHeaders.setContentDisposition(contentDisposition);

        return ResponseEntity.ok()
                .headers(httpHeaders)
                .contentLength(resource.contentLength())
                .contentType(new MediaType("application", "vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(resource);
    }
}
