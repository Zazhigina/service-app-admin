package igc.mirror.question.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import igc.mirror.question.ref.AnswerType;
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
    private boolean isDefault;

    /**
     * Тип ответа
     */
    private AnswerType answerType;

    public AnswerVersionDto() {
    }

    public AnswerVersionDto(String name, Integer orderNo, boolean isDefault, AnswerType answerType) {
        this.name = name;
        this.orderNo = orderNo;
        this.isDefault = isDefault;
        this.answerType = answerType;
    }

    public AnswerVersionDto(String name, Integer orderNo, boolean isDefault, String answerType) {
        this.name = name;
        this.orderNo = orderNo;
        this.isDefault = isDefault;
        this.answerType = AnswerType.valueOf(answerType);
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

    @JsonProperty("isDefault")
    public boolean isDefault() {
        return isDefault;
    }

    public void setDefault(boolean isDefault) {
        this.isDefault = isDefault;
    }

    public AnswerType getAnswerType() {
        return answerType;
    }

    public void setAnswerType(AnswerType answerType) {
        this.answerType = answerType;
    }

    public void setAnswerType(String answerType) {
        this.answerType = AnswerType.valueOf(answerType);
    }
}
