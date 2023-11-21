package igc.mirror.calendar.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;

import java.math.BigDecimal;

@Validated
public class CalendarProductionDto {
    private Long id;

    /**
     * Год
     */
    @Min(value = 1900, message = "Значение года не должно быть меньше 1900")
    @Max(value = 2999, message = "Значение года не должно быть больше 2999")
    @NotNull
    private Integer year;

    /**
     * Рабочее время (в часах)
     */
    private BigDecimal hourWorkCount;

    /**
     * Количество часов работы в месяц
     */
    private BigDecimal monthWorkHourCount;

    public CalendarProductionDto() {
    }

    public Long getId() {
        return id;
    }

    public Integer getYear() {
        return year;
    }

    public BigDecimal getHourWorkCount() {
        return hourWorkCount;
    }

    public BigDecimal getMonthWorkHourCount() {
        return monthWorkHourCount;
    }
}
