package igc.mirror.poi.parser.model;

import lombok.extern.slf4j.Slf4j;

import java.util.regex.Pattern;

@Slf4j
public record DataFilter(String sheetFilter, Integer minRowNum, Integer minColNum, Integer maxRowNum,
                         Integer maxColNum, Pattern sheetFilterPattern) {
    public DataFilter(String sheetFilter, Integer minRowNum, Integer minColNum, Integer maxRowNum, Integer maxColNum) {
        this(
                sheetFilter, minRowNum, minColNum, maxRowNum, maxColNum,
                (sheetFilter != null) ? Pattern.compile(sheetFilter, Pattern.CASE_INSENSITIVE) : null
        );

        if (sheetFilter != null) {
            log.trace("Установлена фильтрация по листам документа [{}]", sheetFilter);
        }

        if (minRowNum != null) {
            log.trace("Установлена фильтрация (начало) по строкам документа {}", this.minRowNum);
        }

        if (minColNum != null) {
            log.trace("Установлена фильтрация (начало) по столбцам документа {}", this.minColNum);
        }

        if (maxRowNum != null) {
            log.trace("Установлена фильтрация (окончание) по строкам документа {}", this.maxRowNum);
        }

        if (maxColNum != null) {
            log.trace("Установлена фильтрация (окончание) по столбцам документа {}", this.maxColNum);
        }
    }
}
