package igc.mirror.question.model;

import igc.mirror.question.ref.QuestionAnnex;
import org.jooq.tools.StringUtils;

import java.time.LocalDateTime;

public class Question {
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
     * Срок действия вопроса
     */
    private LocalDateTime actualTo;

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

    /**
     * Код стандартного вопроса
     */
    private String code;

    /**
     * Приложение к ответу на вопрос
     */
    public QuestionAnnex annex;

    public Question() {
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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public QuestionAnnex getAnnex() {
        return annex;
    }

    public void setAnnex(String annex) {
        this.annex = StringUtils.isEmpty(annex) ? QuestionAnnex.EMPTY : QuestionAnnex.valueOf(annex);
    }
}
