package igc.mirror.variable.dto;

public class VariableDto {

    /**
     * Идентификатор переменной
     */
    private Long id;

    /**
     * Имя переменной
     */
    private String name;

    /**
     * Описание переменной
     */
    private String description;

    /**
     * Значение переменной по умолчанию
     */
    private String defaultVal;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDefaultVal() {
        return defaultVal;
    }

    public void setDefaultVal(String defaultVal) {
        this.defaultVal = defaultVal;
    }
}
