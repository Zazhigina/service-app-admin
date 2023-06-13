package igc.mirror.dto;

import igc.mirror.model.Question;

import java.time.LocalDateTime;
import java.util.List;

public class QuestionDto {

    /**
     * Содержание вопроса
     */
    private String name;

    /**
     * Порядковый номер вопроса
     */
    private Integer orderNo;

    /**
     * Срок действия вопроса
     */
    private LocalDateTime actualTo;

    /**
     * Перечень вариантов ответов на вопрос
     */
    private List<AnswerVersionDto> answerVersions;

    public QuestionDto() {}

    public QuestionDto(Question question) {
        this.name = question.getName();
        this.orderNo = question.getOrderNo();
        this.actualTo = question.getActualTo();
        this.answerVersions = question.getAnswerVersions().stream().map(AnswerVersionDto::new).toList();
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
}
