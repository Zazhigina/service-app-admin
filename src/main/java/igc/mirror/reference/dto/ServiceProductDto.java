package igc.mirror.reference.dto;

import igc.mirror.nsi.model.ServiceProduct;

import java.time.LocalDateTime;

public class ServiceProductDto {

    /**
     * Код услуги
     **/
    private String code;

    /**
     * Наименование услуги
     **/
    private String name;

    /**
     * Начало скрока действия записи
     */
    private LocalDateTime startDate;

    /**
     * Окончание срока действия записи
     */
    private LocalDateTime endDate;

    public ServiceProductDto() {
    }

    public ServiceProductDto(ServiceProduct serviceProduct) {
        this.code = serviceProduct.getCode();
        this.name = serviceProduct.getName();
        this.startDate = serviceProduct.getStartDate();
        this.endDate = serviceProduct.getEndDate();

    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }
}
