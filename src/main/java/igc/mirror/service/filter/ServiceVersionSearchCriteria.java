package igc.mirror.service.filter;

import igc.mirror.utils.qfilter.SearchCriteria;

import java.util.List;

public class ServiceVersionSearchCriteria extends SearchCriteria {

    private List<String> serviceCodes;

    private String version;

    private String newServiceName;

    private String oldServiceName;

    public ServiceVersionSearchCriteria (List<String> serviceCodes,
                                         String       version,
                                         String       newServiceName,
                                         String       oldServiceName){
        this.serviceCodes   = serviceCodes;
        this.version        = version;
        this.newServiceName = newServiceName;
        this.oldServiceName = oldServiceName;
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

    public String getNewServiceName() {
        return newServiceName;
    }

    public void setNewServiceName(String newServiceName) {
        this.newServiceName = newServiceName;
    }

    public String getOldServiceName() {
        return oldServiceName;
    }

    public void setOldServiceName(String oldServiceName) {
        this.oldServiceName = oldServiceName;
    }
}
