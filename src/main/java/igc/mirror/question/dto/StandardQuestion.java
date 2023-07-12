package igc.mirror.question.dto;

import java.util.List;

public class StandardQuestion {

    /**
     * Идентификатор вопроса
     */
    private Long id;

    /**
     * Содержание вопроса
     */
    private String name;

    /**
     * Порядковый номер вопроса
     */
    private Integer orderNo;

    /**
     * Перечень вариантов ответов на вопрос
     */
    private List<StandardAnswerVersion> answerVersions;

    public StandardQuestion() {
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

    public List<StandardAnswerVersion> getAnswerVersions() {
        return answerVersions;
    }

    public void setAnswerVersions(List<StandardAnswerVersion> answerVersions) {
        this.answerVersions = answerVersions;
    }
}
