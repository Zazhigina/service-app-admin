package igc.mirror.rating.controller;

import igc.mirror.rating.service.ReportService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
public class RatingController {
    private final ReportService reportService;

    public RatingController(ReportService reportService) {
        this.reportService = reportService;
    }

    @Operation(summary = "Обратная связь")
    @PostMapping(path = "/rating")
    public ResponseEntity<Resource> getExcelReportRating(@RequestParam(required = false) LocalDate from,
                                                         @RequestParam(required = false) LocalDate to) {
        return reportService.getExcelRatingReport(from, to);
    }

}
