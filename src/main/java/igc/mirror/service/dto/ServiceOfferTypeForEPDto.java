package igc.mirror.service.dto;

import igc.mirror.service.model.ServiceOfferType;

public class ServiceOfferTypeForEPDto {
    private String serviceCode;
    private String offerType;
    private Boolean withCostingDefault;

    public ServiceOfferTypeForEPDto(String serviceCode, String offerType, Boolean withCostingDefault) {
        this.serviceCode = serviceCode;
        this.offerType = offerType;
        this.withCostingDefault = withCostingDefault;
    }
    public static ServiceOfferTypeForEPDto fromModel(ServiceOfferType serviceOfferType){
        return new ServiceOfferTypeForEPDto (serviceOfferType.getServiceCode(),
                String.valueOf(serviceOfferType.getOfferType()),serviceOfferType.getWithCostingDefault());
    }

    public String getServiceCode() {
        return serviceCode;
    }

    public void setServiceCode(String serviceCode) {
        this.serviceCode = serviceCode;
    }

    public String getOfferType() {
        return offerType;
    }

    public void setOfferType(String offerType) {
        this.offerType = offerType;
    }

    public Boolean getWithCostingDefault() {
        return withCostingDefault;
    }

    public void setWithCostingDefault(Boolean withCostingDefault) {
        this.withCostingDefault = withCostingDefault;
    }
}
