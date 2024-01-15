package igc.mirror.service.dto;

import igc.mirror.service.ref.OfferType;

public class ServiceOfferTypeDto {
    /**
     * Идентификатор
     */
    private Long id;
    /**
     * Код услуги
     */
    private String serviceCode;
    /**
     * Наименование услуги
     */
    private String serviceName;
    /**
     * Вид КП
     */
    private OfferType offerType;
    /**
     * Наименование вида КП
     */
    private String offerTypeName;
    /**
     * Запросить расшифровку (по умолчанию)
     */
    private Boolean withCostingDefault;

    public String getServiceCode() {
        return serviceCode;
    }

    public void setServiceCode(String serviceCode) {
        this.serviceCode = serviceCode;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public OfferType getOfferType() {
        return offerType;
    }

    public void setOfferType(OfferType offerType) {
        this.offerType = offerType;
    }

    public String getOfferTypeName() {
        return offerTypeName;
    }

    public void setOfferTypeName(String offerTypeName) {
        this.offerTypeName = offerTypeName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getWithCostingDefault() {
        return withCostingDefault;
    }

    public void setWithCostingDefault(Boolean withCostingDefault) {
        this.withCostingDefault = withCostingDefault;
    }
}
