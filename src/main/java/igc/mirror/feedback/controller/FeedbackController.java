package igc.mirror.feedback.controller;


import igc.mirror.doc.dto.DocumentDto;
import igc.mirror.exception.handler.SuccessInfo;
import igc.mirror.feedback.dto.FeedbackThemeDto;
import igc.mirror.feedback.dto.FeedbackDto;
import igc.mirror.feedback.service.FeedbackReportService;
import igc.mirror.feedback.service.FeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.ResponseEntity;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping("/feedback")
public class FeedbackController {
    @Autowired
    private FeedbackService feedbackService;
    @Autowired
    private FeedbackReportService feedbackReportService;

    @GetMapping("/theme")
    public List<FeedbackThemeDto> findThemes() {
        return feedbackService.findThemes();
    }

    @PostMapping("/theme")
    @PreAuthorize("hasAuthority('CONFIG_VALUE.CHANGE')")
    public FeedbackThemeDto addTheme(@RequestBody FeedbackThemeDto feedbackThemeDto) {
        return feedbackService.addTheme(feedbackThemeDto);
    }

    @PutMapping("/theme/{id}")
    @PreAuthorize("hasAuthority('CONFIG_VALUE.CHANGE')")
    public FeedbackThemeDto changeTheme(@PathVariable Long id, @RequestBody FeedbackThemeDto feedbackThemeDto) {
        return feedbackService.changeTheme(id, feedbackThemeDto);
    }

    @DeleteMapping("/theme/{id}")
    @PreAuthorize("hasAuthority('CONFIG_VALUE.CHANGE')")
    public ResponseEntity<SuccessInfo> deleteTheme(@PathVariable Long id) {
        feedbackService.deleteTheme(id);
        return ResponseEntity.ok(new SuccessInfo("Тема для обращений удалена, ИД: " + id));
    }

    @PutMapping("/theme/sort")
    @PreAuthorize("hasAuthority('CONFIG_VALUE.CHANGE')")
    public List<FeedbackThemeDto> putThemeSort(@RequestBody List<FeedbackThemeDto> sortedThemes) {
        return feedbackService.updateSortTheme(sortedThemes);
    }

    @PostMapping("")
    public ResponseEntity<String> addFeedback(@RequestPart FeedbackDto feedbackDto, @RequestPart(required = false) MultipartFile[] fileBlobs) {
        try {
            feedbackService.addFeedback(feedbackDto, fileBlobs);
            return ResponseEntity.ok("Обращение сохранено");
        }
        catch (IllegalArgumentException e) {
            if(e.getMessage().contains("Неподдерживаемый тип файла: ")) {
                return ResponseEntity.badRequest().body(e.getMessage());
            }
            return ResponseEntity.badRequest().body("");
        }
        catch (Exception e) {
            if(e.getCause().getMessage().contains("Ошибка вызова сервиса документаци")) {
                return ResponseEntity.badRequest().body("Ошибка при сохранении файлов обращения в сервисе документаци");
            }
            return ResponseEntity.badRequest().body("Ошибка при сохранении обращения");
        }
    }

    @GetMapping("/file")
    @PreAuthorize("hasAuthority('CONFIG_VALUE.READ')")
    public ResponseEntity<Resource> getFeedbackFile(@RequestParam UUID uid) {
        DocumentDto document = feedbackService.getFeedbackFile(uid);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentDisposition(document.getContentDisposition());

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .headers(httpHeaders)
                .body(document.getResource());
    }

    @GetMapping("/report")
    @PreAuthorize("hasAuthority('CONFIG_VALUE.READ')")
    public ResponseEntity<Resource> getFeedbackReport(
            @RequestParam(required = false) @DateTimeFormat(pattern="dd.MM.yyyy") LocalDate date1,
            @RequestParam(required = false) @DateTimeFormat(pattern="dd.MM.yyyy") LocalDate date2) {

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        ContentDisposition.attachment()
                                .filename("FeedbackReport.xlsx", StandardCharsets.UTF_8)
                                .build().toString())
                .body(feedbackReportService.generateFeedbackReport(date1, date2));
    }
}
