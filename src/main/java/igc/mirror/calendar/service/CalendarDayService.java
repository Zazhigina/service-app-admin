package igc.mirror.calendar.service;

import igc.mirror.calendar.dto.CalendarDayDto;
import igc.mirror.calendar.filter.CalendarDaySearchCriteria;
import igc.mirror.calendar.mapper.CalendarDayMapper;
import igc.mirror.calendar.model.CalendarDay;
import igc.mirror.calendar.repository.CalendarDayRepository;
import igc.mirror.exception.common.EntityDuplicatedException;
import igc.mirror.utils.qfilter.DataFilter;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Service
@Validated
@Slf4j
public class CalendarDayService {

    private final CalendarDayRepository calendarDayRepository;

    @Autowired
    public CalendarDayService(CalendarDayRepository calendarDayRepository) {
        this.calendarDayRepository = calendarDayRepository;
    }

    /**
     * Возвращает записи справочника рабочих и выходных дней по указанными критериями поиска
     * в соответствии с настройками пагинации
     */
    public Page<CalendarDayDto> findCalendarDaysByFilter(DataFilter<CalendarDaySearchCriteria> filter, Pageable pageable) {
        List<CalendarDayDto> calendarDays = calendarDayRepository.findCalendarDaysByFilter(filter, pageable);

        long total = (calendarDays.size() >= pageable.getPageSize() ?
            calendarDayRepository.getCalendarDaysCount(filter) : calendarDays.size());

        return new PageImpl<>(calendarDays, pageable, total);
    }

    /**
     * Возвращает все записи справочника рабочих и выходных дней
     */
    public List<CalendarDayDto> findAllCalendarDays() {
        return calendarDayRepository.findAllCalendarDays();
    }

    /**
     * Находит запись в справочник рабочих и выходных дней по id
     */
    public CalendarDayDto getCalendarDay(Long id) {
        return CalendarDayMapper.INSTANCE.toDto(calendarDayRepository.getCalendarDay(id));
    }

    /**
     * Добавляет запись в справочник рабочих и выходных дней
     */
    public CalendarDayDto createCalendarDay(@Valid CalendarDayDto deviationDto) {
        log.info("Добавление в справочник рабочих и выходных дней даты {}", deviationDto.getDate());

        CalendarDay calendarDay = CalendarDayMapper.INSTANCE.fromDto(deviationDto);

        try {
            calendarDay = calendarDayRepository.createCalendarDay(calendarDay);
        } catch (DuplicateKeyException e) {
            throw new EntityDuplicatedException("Запись с такой датой уже существует", null, CalendarDay.class);
        }

        return CalendarDayMapper.INSTANCE.toDto(calendarDay);
    }

    /**
     * Изменяет запись в справочнике рабочих и выходных дней
     */
    public CalendarDayDto changeCalendarDay(@Valid CalendarDayDto deviationDto) {
        log.info("Изменение в справочнике рабочих и выходных дней даты {}", deviationDto.getDate());

        CalendarDay calendarDay = CalendarDayMapper.INSTANCE.fromDto(deviationDto);

        try {
            calendarDay = calendarDayRepository.changeCalendarDay(calendarDay);
        } catch (DuplicateKeyException e) {
            throw new EntityDuplicatedException("Запись с такой датой уже существует", null, CalendarDay.class);
        }

        return CalendarDayMapper.INSTANCE.toDto(calendarDay);
    }

    /**
     * Удаляет запись из справочника рабочих и выходных дней
     */
    public void deleteCalendarDay(Long id) {
        log.info("Удаление из справочника рабочих и выходных дней записи с id - {}", id);

        calendarDayRepository.deleteCalendarDay(id);
    }
}
