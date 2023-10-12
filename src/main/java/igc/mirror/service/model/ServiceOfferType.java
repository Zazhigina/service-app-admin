package igc.mirror.service.model;

import igc.mirror.service.dto.ServiceOfferTypeDto;
import igc.mirror.service.ref.OfferType;

import java.time.LocalDateTime;

public class ServiceOfferType {
    /**
     * Идентификатор
     */
    private Long id;
    /**
     * Код услуги
     */
    private String serviceCode;
    /**
     * Вид КП
     */
    private OfferType offerType;
    /**
     * Автор создания
     */
    private String createUser;
    /**
     * Автор изменения
     */
    private String lastUpdateUser;
    /**
     * Дата и время создания
     */
    private LocalDateTime createDate;
    /**
     * Дата и время изменения
     */
    private LocalDateTime lastUpdateDate;

    public ServiceOfferType(){}
    public ServiceOfferType(ServiceOfferTypeDto serviceOfferTypeDto, String user){
        this.id = serviceOfferTypeDto.getId();
        this.serviceCode = serviceOfferTypeDto.getServiceCode();
        this.offerType = serviceOfferTypeDto.getOfferType();
        fillAuthInfo(user);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getServiceCode() {
        return serviceCode;
    }

    public void setServiceCode(String serviceCode) {
        this.serviceCode = serviceCode;
    }

    public OfferType getOfferType() {
        return offerType;
    }

    public void setOfferType(OfferType offerType) {
        this.offerType = offerType;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public String getLastUpdateUser() {
        return lastUpdateUser;
    }

    public void setLastUpdateUser(String lastUpdateUser) {
        this.lastUpdateUser = lastUpdateUser;
    }

    public LocalDateTime getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDateTime createDate) {
        this.createDate = createDate;
    }

    public LocalDateTime getLastUpdateDate() {
        return lastUpdateDate;
    }

    public void setLastUpdateDate(LocalDateTime lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }

    public void fillAuthInfo(String userName){
        this.lastUpdateUser = userName;
        if (this.id == null) {
            this.createUser = userName;
            this.createDate = LocalDateTime.now();
            this.lastUpdateDate = LocalDateTime.now();
        } else {
            this.lastUpdateDate = LocalDateTime.now();
        }
    }
}
