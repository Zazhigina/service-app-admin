package igc.mirror.model;

import jakarta.validation.constraints.NotBlank;

import java.util.List;
import java.time.LocalDateTime;

public class Question {
    private Long id;

    /**
     * Содержание вопроса
     */
    @NotBlank(message = "Содержимое вопроса не должно быть пустым")
    private String name;

    /**
     * Порядковый номер вопроса
     */
    @NotBlank(message = "Порядковый номер вопроса не может быть пустым")
    private Integer orderNo;

    /**
     * Срок действия вопроса
     */
    private LocalDateTime actualTo;

    /**
     * Перечень вариантов ответов на вопрос
     */
    private List<AnswerVersion> answerVersions;

    /**
     * Дата и время создания документа
     */
    private LocalDateTime createDate;

    /**
     * Автор создания
     */
    private String createUser;

    /**
     * Дата и время последнего изменения
     */
    private LocalDateTime lastUpdateDate;

    /**
     * Автор изменения
     */
    private String lastUpdateUser;

    public Question() {}

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

    public LocalDateTime getActualTo() {
        return actualTo;
    }

    public void setActualTo(LocalDateTime actualTo) {
        this.actualTo = actualTo;
    }

    public LocalDateTime getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDateTime createDate) {
        this.createDate = createDate;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public LocalDateTime getLastUpdateDate() {
        return lastUpdateDate;
    }

    public void setLastUpdateDate(LocalDateTime lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }

    public String getLastUpdateUser() {
        return lastUpdateUser;
    }

    public void setLastUpdateUser(String lastUpdateUser) {
        this.lastUpdateUser = lastUpdateUser;
    }

    public List<AnswerVersion> getAnswerVersions() {
        return answerVersions;
    }

    public void setAnswerVersions(List<AnswerVersion> answerVersions) {
        this.answerVersions = answerVersions;
    }
}
