package igc.mirror.question.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import igc.mirror.question.ref.AnswerType;

public class StandardAnswerVersion {

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
    private Boolean isDefault;

    /**
     * Тип ответа
     */
    private AnswerType answerType;

    public StandardAnswerVersion() {
    }

    public StandardAnswerVersion(Long id, String name, Integer orderNo, Boolean isDefault, AnswerType answerType) {
        this.id = id;
        this.name = name;
        this.orderNo = orderNo;
        this.isDefault = isDefault;
        this.answerType = answerType;
    }

    public StandardAnswerVersion(Long id, String name, Integer orderNo, Boolean isDefault, String answerType) {
        this.id = id;
        this.name = name;
        this.orderNo = orderNo;
        this.isDefault = isDefault;
        this.answerType = AnswerType.valueOf(answerType);
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

    @JsonProperty("isDefault")
    public Boolean isDefault() {
        return isDefault;
    }

    public void setDefault(Boolean isDefault) {
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
