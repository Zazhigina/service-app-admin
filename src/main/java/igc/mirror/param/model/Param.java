package igc.mirror.param.model;

import jooqdata.tables.pojos.AParam;

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

    public Param(String key, String name, String val, String defaultValue){
        super();
        super.setKey(key);
        super.setName(name);
        super.setVal(val);
        super.setDefaultVal(defaultValue);
    }
}
