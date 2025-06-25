package igc.mirror.calendar.model;

import igc.mirror.calendar.dto.CalendarProductionDto;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;

public class CalendarProduction {
    private Long id;

    /**
     * Год
     */
    private Integer year;

    /**
     * Рабочее время (в часах)
     */
    private BigDecimal hourWorkCount;

    /**
     * Количество часов работы в месяц
     */
    private BigDecimal monthWorkHourCount;

    /**
     * Дата и время создания документа
     */
    private LocalDateTime createDate;

    /**
     * Автор создания
     */
    private String createUser;

    /**
     * Дата и время последнего изменения
     */
    private LocalDateTime lastUpdateDate;

    /**
     * Автор изменения
     */
    private String lastUpdateUser;

    public CalendarProduction() {}

    public CalendarProduction(Long id, Integer year, BigDecimal hourWorkCount, BigDecimal monthWorkHourCount,
                              String user) {
        this.id = id;
        this.year = year;
        this.hourWorkCount = hourWorkCount;
        this.monthWorkHourCount = hourWorkCount != null ? hourWorkCount.divide(BigDecimal.valueOf(12), 1, RoundingMode.HALF_UP) : null;
        if (id == null) this.createUser = user;
        else this.lastUpdateUser = user;
    }

    public Long getId() {
        return id;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public BigDecimal getHourWorkCount() {
        return hourWorkCount;
    }

    public void setHourWorkCount(BigDecimal hourWorkCount) {
        this.hourWorkCount = hourWorkCount;
    }

    public BigDecimal getMonthWorkHourCount() {
        return monthWorkHourCount;
    }

    public void setMonthWorkHourCount(BigDecimal monthWorkHourCount) {
        this.monthWorkHourCount = monthWorkHourCount;
    }

    public LocalDateTime getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDateTime createDate) {
        this.createDate = createDate;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public LocalDateTime getLastUpdateDate() {
        return lastUpdateDate;
    }

    public void setLastUpdateDate(LocalDateTime lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }

    public String getLastUpdateUser() {
        return lastUpdateUser;
    }

    public void setLastUpdateUser(String lastUpdateUser) {
        this.lastUpdateUser = lastUpdateUser;
    }

    public static CalendarProduction fromDto(CalendarProductionDto calendarProduction, String user) {
        return new CalendarProduction(
                calendarProduction.getId(),
                calendarProduction.getYear(),
                calendarProduction.getHourWorkCount(),
                calendarProduction.getMonthWorkHourCount(),
                user);
    }
}