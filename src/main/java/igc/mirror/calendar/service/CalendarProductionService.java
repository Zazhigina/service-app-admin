package igc.mirror.calendar.service;

import igc.mirror.auth.UserDetails;
import igc.mirror.calendar.dto.CalendarProductionDto;
import igc.mirror.calendar.filter.CalendarProductionSearchCriteria;
import igc.mirror.calendar.model.CalendarProduction;
import igc.mirror.calendar.repository.CalendarProductionRepository;
import igc.mirror.exception.handler.EntityExceptionInfo;
import igc.mirror.exception.handler.GroupProcessInfo;
import igc.mirror.utils.qfilter.DataFilter;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Validated
public class CalendarProductionService {
    static final Logger logger = LoggerFactory.getLogger(CalendarProductionService.class);

    @Autowired
    CalendarProductionRepository calendarProductionRepository;
    @Autowired
    private UserDetails userDetails;

    /**
     * Сохраняет данные производственного календаря
     *
     * @param calendarProductionItems данные
     */
    @PreAuthorize("hasAuthority('APP_ADMIN.EXEC')")
    public GroupProcessInfo saveCalendarProduction(@Valid List<CalendarProductionDto> calendarProductionItems) {
        logger.info("Сохранение данных производственного календаря: {}", calendarProductionItems);

        Map<Boolean, List<CalendarProduction>> calendarByExists = calendarProductionItems.stream()
                .map(calendarProductionItem -> CalendarProduction.fromDto(calendarProductionItem, userDetails.getUsername())
                )
                .collect(Collectors.partitioningBy(item -> item.getId() != null));

        List<EntityExceptionInfo> exceptions = new ArrayList<>();

        List<CalendarProduction> savedItems = new ArrayList<>();

        // добавляем новые строки
        if (!CollectionUtils.isEmpty(calendarByExists.get(Boolean.FALSE)))
            for (CalendarProduction calendarProduction : calendarByExists.get(Boolean.FALSE))
                try {
                    savedItems.add(calendarProductionRepository.addCalendarProduction(calendarProduction));
                } catch (DuplicateKeyException ex) {
                    exceptions.add(new EntityExceptionInfo("Данные для года " + calendarProduction.getYear() + " уже введены",
                            null, null, CalendarProduction.class));
                } catch (Exception e) {
                    exceptions.add(new EntityExceptionInfo("Ошибка при добавлении записи производственного календаря: " + e,
                            null, null, CalendarProduction.class));
                }

        // обновляем старые
        if (!CollectionUtils.isEmpty(calendarByExists.get(Boolean.TRUE)))
            for (CalendarProduction calendarProduction : calendarByExists.get(Boolean.TRUE))
                try {
                    savedItems.add(calendarProductionRepository.updateCalendarProduction(calendarProduction));
                } catch (DuplicateKeyException ex) {
                    exceptions.add(new EntityExceptionInfo("Данные для года " + calendarProduction.getYear() + " уже введены",
                            null, calendarProduction.getId(), CalendarProduction.class));
                } catch (Exception e) {
                    exceptions.add(new EntityExceptionInfo("Ошибка при изменении записи производственного календаря: " + e,
                            null, calendarProduction.getId(), CalendarProduction.class));
                }

        return new GroupProcessInfo("Сохранение данных производственного календаря", savedItems.size(), exceptions);
    }

    /**
     * Находит все данные производственного календаря
     *
     * @return все данные производственного календаря
     */
    public List<CalendarProductionDto> findCalendarProduction() {
        logger.info("Получение всех данных производственного календаря");

        return calendarProductionRepository.findCalendarProduction();
    }

    /**
     * Находит данные производственного календаря за указанный год
     *
     * @param year Год
     * @return данные
     */
    public CalendarProductionDto findCalendarProduction(Integer year) {
        logger.info("Получение данных производственного календаря за {} год", year);

        return calendarProductionRepository.findCalendarProduction(year);
    }

    /**
     * Удаляет запись производственного календаря
     *
     * @param id идентификатор записи
     */
    @PreAuthorize("hasAuthority('APP_ADMIN.EXEC')")
    public void deleteCalendarProduction(Long id) {
        logger.info("Удаление записи производственного календаря с ИД - {}", id);

        calendarProductionRepository.deleteCalendarProduction(id);
    }

    /**
     * Возвращает список данных производственного календаря по указанными критериями поиска
     * в соответствии с настройками пагинации
     *
     * @param filter   критерий поиска
     * @param pageable настройки пагинации
     * @return данные производственного календаря
     */
    public Page<CalendarProductionDto> findCalendarProductionByFilter(DataFilter<CalendarProductionSearchCriteria> filter, Pageable pageable) {
        List<CalendarProductionDto> calendarProduction = calendarProductionRepository.findCalendarProductionByFilter(filter, pageable);

        long total = (calendarProduction.size() >= pageable.getPageSize() ?
                calendarProductionRepository.getCalendarProductionItemsCount(filter) : calendarProduction.size());

        return new PageImpl<>(calendarProduction, pageable, total);
    }
}
