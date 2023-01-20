package igc.mirror.model;

import jooqdata.tables.pojos.AParam;

import java.time.LocalDateTime;

public class Param extends AParam {
    public Param() {
        super();
    }

    public Param(String key, String name, String val){
        super();
        super.setKey(key);
        super.setName(name);
        super.setVal(val);
    }

    public Param(String key, String name, String val, LocalDateTime createDate, String createUser, LocalDateTime lastUpdateDate, String lastUpdateUser) {
        super(key, name, val, createDate, createUser, lastUpdateDate, lastUpdateUser);
    }
}
