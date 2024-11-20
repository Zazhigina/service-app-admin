package igc.mirror.matrix.dto;

import igc.mirror.matrix.model.Org;

public class OrgDTO {
    /**
     * Id организатора
     */
    private Long id;

    /**
     * Код организатора
     */
    private String orgCode;

    /**
     * Название организатора
     */
    private String orgName;

    /**
     * Блок организатора
     */
    private String orgBlock;

    /**
     * Компания организатора
     */
    private String companyCode;

    public OrgDTO(Long id, String orgCode, String orgName, String orgBlock, String companyCode) {
        this.id         = id;
        this.orgCode    = orgCode;
        this.orgName    = orgName;
        this.orgBlock   = orgBlock;
        this.companyCode = companyCode;
    }

    public OrgDTO(Org org) {
        this.orgCode    = org.getCode();
        this.orgName    = org.getName();
        this.orgBlock   = org.getBlock();
        this.companyCode = org.getCompanyCode();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOrgCode() {
        return orgCode;
    }

    public void setOrgCode(String orgCode) {
        this.orgCode = orgCode;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getOrgBlock() {
        return orgBlock;
    }

    public void setOrgBlock(String orgBlock) {
        this.orgBlock = orgBlock;
    }

    public String getCompanyCode() {
        return companyCode;
    }

    public void setCompanyCode(String companyCode) {
        this.companyCode = companyCode;
    }
}
