package igc.mirror.calendar.controller;

import igc.mirror.calendar.dto.CalendarProductionDto;
import igc.mirror.calendar.service.CalendarProductionService;
import igc.mirror.exception.handler.GroupProcessInfo;
import igc.mirror.exception.handler.SuccessInfo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("calendar-production")
@Tag(name = "Производственный календарь")
public class CalendarProductionController {
    @Autowired
    CalendarProductionService calendarProductionService;

    @Operation(summary = "Сохранение данных производственного календаря")
    @PutMapping()
    public GroupProcessInfo saveCalendarProduction(@RequestBody List<CalendarProductionDto> calendarProductionItems) {
        return calendarProductionService.saveCalendarProduction(calendarProductionItems);
    }

    @Operation(summary = "Получение всех данных производственного календаря")
    @GetMapping()
    public ResponseEntity<List<CalendarProductionDto>> findAllCalendarProduction() {
        return ResponseEntity.ok(calendarProductionService.findCalendarProduction());
    }

    @Operation(summary = "Получение данных производственного календаря по году")
    @GetMapping("/by-year/{year}")
    public ResponseEntity<CalendarProductionDto> findCalendarProductionByYear(@PathVariable("year") Integer year) {
        return ResponseEntity.ok(calendarProductionService.findCalendarProduction(year));
    }

    @Operation(summary = "Удаление данных производственного календаря по идентификатору записи")
    @DeleteMapping("/{id}")
    public ResponseEntity<SuccessInfo> deleteCalendarProduction(@PathVariable Long id) {
        calendarProductionService.deleteCalendarProduction(id);
        return ResponseEntity.ok(new SuccessInfo("Удалена запись производстенного календаря, ИД: " + id));
    }
}
