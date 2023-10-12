package igc.mirror.service.dto;

import igc.mirror.service.ref.OfferType;

public class OfferTypeDto {
    public String code;
    public String name;

    public OfferTypeDto(OfferType offerType){
        this.code = offerType.name();
        this.name = offerType.getName();
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
}
