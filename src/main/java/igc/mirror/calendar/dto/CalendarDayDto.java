package igc.mirror.calendar.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
public class CalendarDayDto {
    private Long id;

    @NotNull(message = "Не указана дата")
    private LocalDate date;

    @NotNull(message = "Не указан признак рабочий/выходной день")
    private Boolean working;
}
