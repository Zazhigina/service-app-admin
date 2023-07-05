package igc.mirror.question.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class AnswerVersionDto {

    /**
     * Вариант ответа
     */
    @NotBlank(message = "Текст варианта ответа не должен быть пустым")
    private String name;

    /**
     * Порядковый номер ответа
     */
    @NotNull(message = "Порядковый номер варианта ответа не должен быть пустым")
    private Integer orderNo;

    /**
     * Вариант ответа выбран по умолчанию
     */
    @JsonProperty("isDefault")
    private boolean isDefault;

    public AnswerVersionDto() {
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
