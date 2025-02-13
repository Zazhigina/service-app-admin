package igc.mirror.calendar.controller;

import igc.mirror.calendar.dto.CalendarDayDto;
import igc.mirror.calendar.filter.CalendarDaySearchCriteria;
import igc.mirror.calendar.service.CalendarDayService;
import igc.mirror.exception.handler.SuccessInfo;
import igc.mirror.utils.qfilter.DataFilter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("calendar-day")
@Tag(name = "Производственный календарь (рабочие и выходные дни)")
public class CalendarDayController {
    private final CalendarDayService calendarDayService;

    @Autowired
    public CalendarDayController(CalendarDayService calendarDayService) {
        this.calendarDayService = calendarDayService;
    }

    @Operation(summary = "Получение рабочих и выходных дней по указанному фильтру")
    @PostMapping(path = "/filter")
    public Page<CalendarDayDto> findCalendarDay(@RequestBody(required = false) DataFilter<CalendarDaySearchCriteria> filter, Pageable pageable) {
        return calendarDayService.findCalendarDaysByFilter(filter, pageable);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получение записи о рабочих и выходных днях")
    public CalendarDayDto getCalendarDay(@PathVariable Long id) {
        return calendarDayService.getCalendarDay(id);
    }

    @PostMapping()
    @Operation(summary = "Создание записи о рабочих и выходных днях")
    public CalendarDayDto createCalendarDay(@RequestBody CalendarDayDto calendarDay) {
        return calendarDayService.createCalendarDay(calendarDay);
    }

    @PutMapping()
    @Operation(summary = "Изменение записи о рабочих и выходных днях")
    public CalendarDayDto changeCalendarDay(@RequestBody CalendarDayDto calendarDay) {
        return calendarDayService.changeCalendarDay(calendarDay);
    }

    @Operation(summary = "Удаление записи о рабочих и выходных днях")
    @DeleteMapping("/{id}")
    public SuccessInfo deleteCalendarDay(@PathVariable Long id) {
        calendarDayService.deleteCalendarDay(id);
        return new SuccessInfo("Запись удалена");
    }
}
