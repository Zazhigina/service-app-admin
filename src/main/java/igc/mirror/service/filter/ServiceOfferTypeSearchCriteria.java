package igc.mirror.service.filter;

import igc.mirror.utils.qfilter.SearchCriteria;

public class ServiceOfferTypeSearchCriteria  extends SearchCriteria {
    private String serviceCode;

    public String getServiceCode() {
        return serviceCode;
    }

    public void setServiceCode(String serviceCode) {
        this.serviceCode = serviceCode;
    }

    public ServiceOfferTypeSearchCriteria() {
        super();
    }
}
