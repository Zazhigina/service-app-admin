package igc.mirror.service.dto;

import igc.mirror.nsi.model.ServiceProduct;

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

    public ServiceProductDto() {
    }

    public ServiceProductDto(ServiceProduct serviceProduct) {
        this.code = serviceProduct.getCode();
        this.name = serviceProduct.getName();
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
}
