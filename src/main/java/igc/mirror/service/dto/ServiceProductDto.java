package igc.mirror.service.dto;

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
     * Код каталога
     */
    private Boolean offerTypeExists;

    /**
     * Код устарел
     */
    private Boolean outdated;

    public ServiceProductDto() {
    }

    public ServiceProductDto(ServiceProduct serviceProduct) {
        this.code = serviceProduct.getCode();
        this.name = serviceProduct.getName();

        if (serviceProduct.getEndDate() != null && LocalDateTime.now().isAfter(serviceProduct.getEndDate()))
            outdated = true;
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

    public Boolean getOfferTypeExists() {
        return offerTypeExists;
    }

    public void setOfferTypeExists(Boolean offerTypeExists) {
        this.offerTypeExists = offerTypeExists;
    }

    public Boolean getOutdated() {
        return outdated;
    }
}
