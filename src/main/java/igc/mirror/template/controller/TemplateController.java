package igc.mirror.template.controller;

import igc.mirror.doc.dto.DocumentDto;
import igc.mirror.exception.handler.SuccessInfo;
import igc.mirror.template.dto.LetterTemplateBriefInfoDto;
import igc.mirror.template.dto.LetterTemplateDto;
import igc.mirror.template.dto.LetterTemplateTypeDto;
import igc.mirror.template.dto.TemplateDto;
import igc.mirror.template.filter.LetterTemplateSearchCriteria;
import igc.mirror.template.service.TemplateService;
import igc.mirror.utils.qfilter.DataFilter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("letter-template")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Шаблоны")
public class TemplateController {
    private final TemplateService templateService;

    @Autowired
    public TemplateController(TemplateService templateService) {
        this.templateService = templateService;
    }

    @PostMapping("/filter")
    @Operation(summary = "Поиск шаблонов с фильтром")
    public Page<LetterTemplateBriefInfoDto> findLetterTemplatesByFilter(@RequestBody(required = false) DataFilter<LetterTemplateSearchCriteria> filter, Pageable pageable) {
        return templateService.findLetterTemplateBriefs(filter, pageable);
    }

    @GetMapping("/by-letter-type")
    @Operation(summary = "Поиск информации о шаблоне по наименованию параметра")
    public ResponseEntity<LetterTemplateDto> findLetterTemplateByLetterType(@RequestParam("letterType") String letterType) {
        return ResponseEntity.ok(templateService.findByLetterType(letterType));
    }

    @GetMapping("/{id}/doc")
    @Operation(summary = "Выгрузка шаблона")
    public ResponseEntity<Resource> downloadLetterTemplateDoc(@PathVariable Long id,
                                                              @RequestParam(required = false, defaultValue = "false") Boolean isFileId) {
        DocumentDto document = templateService.downloadLetterTemplateDoc(id, isFileId);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentDisposition(document.getContentDisposition());

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .headers(httpHeaders)
                .body(document.getResource());
    }

    @PostMapping(value = "/{id}", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    @Operation(summary = "Изменение данных шаблона")
    @PreAuthorize("hasAuthority('APP_ADMIN.EXEC')")
    public ResponseEntity<SuccessInfo> changeLetterTemplate(@PathVariable Long id,
                                                            @RequestPart("request") LetterTemplateDto letterTemplateRequest,
                                                            @RequestPart(required = false, name = "file") MultipartFile file) {
        templateService.changeLetterTemplate(id, letterTemplateRequest, file);
        return ResponseEntity.ok(new SuccessInfo("Операция выполнена успешно"));
    }

    @GetMapping("/type")
    @Operation(summary = "Поиск типов шаблонов")
    public List<LetterTemplateTypeDto> findLetterTemplateTypes() {
        return templateService.findLetterTemplateTypes();
    }

    @Deprecated
    @GetMapping("/{id}/doc/info")
    @Operation(summary = "Данные загруженного документа шаблона")
    public ResponseEntity<DocumentDto> getLetterTemplateDocInfo(@PathVariable Long id) {
        return ResponseEntity.ok(templateService.getLetterTemplateDocInfo(id));
    }

    @GetMapping("/by-param")
    @Operation(summary = "Запрос шаблона по наименованию параметра")
    public ResponseEntity<TemplateDto> retrieveTemplate(@RequestParam("param") String letterType) {
        return ResponseEntity.ok(templateService.retrieveTemplate(letterType));
    }
}
