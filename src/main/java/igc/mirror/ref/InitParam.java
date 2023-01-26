package igc.mirror.ref;

public enum InitParam {
    SEARCH_BY_SUBJECT_MIN_THRESHOLD("SEARCH_BY_SUBJECT.MIN_THRESHOLD", "Поиск по предмету закупок, минимальный порог в % для поиска уч-ков", 65),

    SEARCH_BY_SUBJECT_MAX_CONTRACTOR("SEARCH_BY_SUBJECT.MAX_CONTRACTOR", "Поиск по предмету закупок, максимальное число уч-ков в выборке", 100),

    SEARCH_BY_OKPD_MIN_THRESHOLD("SEARCH_BY_OKPD.MIN_THRESHOLD", "Поиск по ОКПД2 минимальный порог в % для поиска уч-ков", 65),

    SEARCH_BY_OKPD_MAX_CONTRACTOR("SEARCH_BY_OKPD.MAX_CONTRACTOR", "Поиск по ОКПД2, максимальное число уч-ков в выборке", 100),

    SEARCH_BY_OKVED_MIN_THRESHOLD("SEARCH_BY_OKVED.MIN_THRESHOLD", "Поиск по ОКВЭД2, минимальный порог в % для поиска уч-ков", 65),

    SEARCH_BY_OKVED_MAX_CONTRACTOR("SEARCH_BY_OKVED.MAX_CONTRACTOR", "Поиск по ОКВЭД2, максимальное число уч-ков в выборке", 100),

    SEARCH_BY_SERVICE_MIN_THRESHOLD("SEARCH_BY_SERVICE.MIN_THRESHOLD", "Поиск по услуге, минимальный порог в % для поиска уч-ков", 65),

    SEARCH_BY_SERVICE_MAX_CONTRACTOR("SEARCH_BY_SERVICE.MAX_CONTRACTOR", "Поиск по услуге, максимальное число уч-ков в выборке", 100);

    private final String key;
    private final String name;
    private final Integer defaultValue;

    InitParam(String key, String name, Integer defaultValue) {
        this.key = key;
        this.name = name;
        this.defaultValue = defaultValue;
    }

    public String getKey() {
        return key;
    }

    public String getName() {
        return name;
    }

    public Integer getDefaultValue() {
        return defaultValue;
    }

    public static InitParam valueOfKey(String key){
        for (InitParam initParam : InitParam.values()){
            if(initParam.key.equals(key)) {
                return initParam;
            }
        }
        return null;
    }
}
