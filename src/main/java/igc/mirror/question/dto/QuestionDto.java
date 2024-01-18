package igc.mirror.question.dto;

import igc.mirror.question.ref.QuestionOwner;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.List;

public class QuestionDto {

    /**
     * Содержание вопроса
     */
    @NotBlank(message = "Содержимое вопроса не должно быть пустым")
    private String name;

    /**
     * Порядковый номер вопроса
     */
    @NotNull(message = "Порядковый номер вопроса не может быть пустым")
    private Integer orderNo;

    /**
     * Срок действия вопроса
     */
    private LocalDateTime actualTo;

    /**
     * Код стандартного вопроса
     */
    private String code;

    /**
     * Перечень вариантов ответов на вопрос
     */
    private List<AnswerVersionDto> answerVersions;

    private QuestionOwner owner;

    public QuestionDto() {
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

    public LocalDateTime getActualTo() {
        return actualTo;
    }

    public void setActualTo(LocalDateTime actualTo) {
        this.actualTo = actualTo;
    }

    public List<AnswerVersionDto> getAnswerVersions() {
        return answerVersions;
    }

    public void setAnswerVersions(List<AnswerVersionDto> answerVersions) {
        this.answerVersions = answerVersions;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public QuestionOwner getOwner() {
        return owner;
    }

    public void setOwner(QuestionOwner owner) {
        this.owner = owner;
    }
}
