package igc.mirror.matrix.model;

public class Org {

    /**
     * Код организатора
     */
    private String orgCode;

    /**
     * Наименование организатора
     */
    private String orgName;

    /**
     * Блок
     */
    private String orgBlock;

    /**
     * Код компании
     */
    private String companyCode;

    public Org() {}

    public String getCode() {
        return orgCode;
    }

    public void setCode(String orgCode) {
        this.orgCode = orgCode;
    }

    public String getName() {
        return orgName;
    }

    public void setName(String orgName) {
        this.orgName = orgName;
    }

    public String getBlock() {
        return orgBlock;
    }

    public void setBlock(String orgBlock) {
        this.orgBlock = orgBlock;
    }

    public String getCompanyCode() {
        return companyCode;
    }

    public void setCompanyCode(String companyCode) {
        this.companyCode = companyCode;
    }
}
