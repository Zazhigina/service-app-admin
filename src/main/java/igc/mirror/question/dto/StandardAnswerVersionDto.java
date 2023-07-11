package igc.mirror.question.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class StandardAnswerVersionDto {

    /**
     * Идентификатор ответа
     */
    private Long id;

    /**
     * Вариант ответа
     */
    private String name;

    /**
     * Порядковый номер ответа
     */
    private Integer orderNo;

    /**
     * Вариант ответа выбран по умолчанию
     */
    @JsonProperty("isDefault")
    private boolean isDefault;

    public StandardAnswerVersionDto() {
    }

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

    public Integer getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(Integer orderNo) {
        this.orderNo = orderNo;
    }

    public boolean isDefault() {
        return isDefault;
    }

    public void setDefault(boolean isDefault) {
        this.isDefault = isDefault;
    }
}
