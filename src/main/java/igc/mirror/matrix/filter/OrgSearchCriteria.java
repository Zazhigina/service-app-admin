package igc.mirror.matrix.filter;

import java.time.LocalDateTime;

public class OrgSearchCriteria extends MatrixSearchCriteria {
    private String companyCode;

    public OrgSearchCriteria(LocalDateTime fromDate, LocalDateTime toDate, String searchKey) {
        super(fromDate, toDate, searchKey);
    }

    public String getCompanyCode() {
        return companyCode;
    }

    public void setCompanyCode(String companyCode) {
        this.companyCode = companyCode;
    }
}
