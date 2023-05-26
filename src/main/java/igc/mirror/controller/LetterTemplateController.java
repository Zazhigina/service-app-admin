package igc.mirror.controller;

import igc.mirror.doc.dto.DocumentDto;
import igc.mirror.dto.LetterTemplateDto;
import igc.mirror.exception.handler.SuccessInfo;
import igc.mirror.filter.LetterTemplateSearchCriteria;
import igc.mirror.service.LetterTemplateService;
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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("letter-template")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Шаблоны")
public class LetterTemplateController {
    private final LetterTemplateService letterTemplateService;

    @Autowired
    public LetterTemplateController(LetterTemplateService letterTemplateService) {
        this.letterTemplateService = letterTemplateService;
    }

    @PostMapping("/filter")
    @Operation(summary = "Поиск шаблонов с фильтром")
    public Page<LetterTemplateDto> findLetterTemplatesByFilter(@RequestBody(required = false) DataFilter<LetterTemplateSearchCriteria> filter, Pageable pageable){
        return letterTemplateService.findLetterTemplatesByFilters(filter,pageable);
    }

    @GetMapping("/by-letter-type")
    @Operation(summary = "Поиск шаблона по наименованию параметра")
    public ResponseEntity<LetterTemplateDto> findLetterTemplateByLetterType(@RequestParam("letterType") String letterType){
        return ResponseEntity.ok(letterTemplateService.findByLetterType(letterType));
    }

    @GetMapping("/{id}/doc")
    @Operation(summary = "Выгрузка шаблона")
    public ResponseEntity<Resource> downloadLetterTemplate(@PathVariable Long id){
        DocumentDto document = letterTemplateService.downloadLetterTemplate(id);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentDisposition(document.getContentDisposition());

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .headers(httpHeaders)
                .body(document.getResource());
    }

    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    @Operation(summary = "Сохранение нового шаблона")
    public ResponseEntity<LetterTemplateDto> saveLetterTemplate(@RequestPart("request")LetterTemplateDto letterTemplateRequest,
                                                                @RequestPart("file")MultipartFile file){
        return ResponseEntity.ok(letterTemplateService.saveLetterTemplate(letterTemplateRequest, file));
    }

    @PutMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE}, value = "/doc/{id}")
    @Operation(summary = "Замена файла шаблона")
    public ResponseEntity<SuccessInfo> replaceLetterTemplateDoc(@PathVariable Long id, @RequestPart("file")MultipartFile file){
        letterTemplateService.replaceLetterTemplate(id, file);
        return ResponseEntity.ok(new SuccessInfo("Операция выполнена успешно"));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Изменение данных шаблона")
    public ResponseEntity<LetterTemplateDto> changeLetterTemplate(@PathVariable Long id, @RequestBody LetterTemplateDto letterTemplateRequest){
        return ResponseEntity.ok(letterTemplateService.changeLetterTemplate(id, letterTemplateRequest));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удаление шаблона")
    public ResponseEntity<SuccessInfo> deleteLetterTemplate(@PathVariable Long id){
        letterTemplateService.deleteLetterTemplate(id);
        return ResponseEntity.ok(new SuccessInfo("Операция выполнена успешно"));
    }

    @GetMapping("/{id}/doc/info")
    @Operation(summary = "Данные загруженного документа шаблона")
    public ResponseEntity<DocumentDto> getLetterTemplateDocInfo(@PathVariable Long id){
        return ResponseEntity.ok(letterTemplateService.getLetterTemplateDocInfo(id));
    }
}
