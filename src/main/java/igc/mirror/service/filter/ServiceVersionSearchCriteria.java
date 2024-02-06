package igc.mirror.service.filter;

import igc.mirror.utils.qfilter.SearchCriteria;

import java.util.List;

public class ServiceVersionSearchCriteria extends SearchCriteria {

    private List<String> serviceCodes;

    private String version;

    public ServiceVersionSearchCriteria (List<String> serviceCodes, String  version){
        this.serviceCodes = serviceCodes;
        this.version      = version;
    }

    public List<String> getServiceCodes() {
        return serviceCodes;
    }

    public void setServiceCodes(List<String> serviceCodes) {
        this.serviceCodes = serviceCodes;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}
