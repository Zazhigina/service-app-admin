package igc.mirror.rating.controller;

import igc.mirror.auth.AuthorityConstants;
import igc.mirror.rating.service.ReportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@SecurityRequirement(name = "bearerAuth")
public class RatingController {
    private final ReportService reportService;

    public RatingController(ReportService reportService) {
        this.reportService = reportService;
    }

    @Operation(summary = "Обратная связь")
    @PostMapping(path = "/rating")
    @PreAuthorize(AuthorityConstants.PreAuthorize.CONFIG_VALUE_READ)
    public ResponseEntity<Resource> getExcelReportRating(@RequestParam(required = false) LocalDate from,
                                                         @RequestParam(required = false) LocalDate to) {
        return reportService.getExcelRatingReport(from, to);
    }

}
