package igc.mirror.version.controller;

import igc.mirror.exception.handler.SuccessInfo;
import igc.mirror.utils.qfilter.DataFilter;
import igc.mirror.version.dto.PreparedRequestVersionRatingDto;
import igc.mirror.version.dto.RequestVersionProcessingStatus;
import igc.mirror.version.filter.IncomingRequestVersionRatingSearchCriteria;
import igc.mirror.version.service.RequestVersionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/request")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Обработка оценок версий поиска")
public class RequestVersionController {
    private final RequestVersionService requestVersionService;

    public RequestVersionController(RequestVersionService requestVersionService) {
        this.requestVersionService = requestVersionService;
    }

    @Operation(summary = "Список оценок версий поиска")
    @PostMapping(path = "/version-rating/filter")
    public Page<PreparedRequestVersionRatingDto> findRequestVersionRatingsByFilter(@RequestBody(required = false) DataFilter<IncomingRequestVersionRatingSearchCriteria> filter,
                                                                                   Pageable pageable) {
        return requestVersionService.findRequestVersionRatingsByFilter(filter, pageable);
    }

    @Operation(summary = "Изменение статуса обработки версии поиска")
    @PutMapping(path = "/version/processing-status")
    public ResponseEntity<SuccessInfo> changeRequestVersionProcessingStatus(@RequestBody RequestVersionProcessingStatus versionProcessingStatus) {
        return new ResponseEntity<>(requestVersionService.changeRequestVersionProcessingStatus(versionProcessingStatus), HttpStatus.OK);
    }
}
