package igc.mirror.question.model;

import igc.mirror.question.ref.AnswerType;

import java.time.LocalDateTime;

public class AnswerVersion {
   private Long id;

   /**
    * Идентификатор вопроса
    */
   private Long questionId;

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
   private boolean isDefault;

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
    * Тип ответа
    */
   private AnswerType answerType;

   public AnswerVersion() {}

   public AnswerVersion(Long id, Long questionId, String name, Integer orderNo, boolean isDefault,
                        LocalDateTime createDate, String createUser, LocalDateTime lastUpdateDate,
                        String lastUpdateUser, AnswerType answerType) {
      this.id = id;
      this.questionId = questionId;
      this.name = name;
      this.orderNo = orderNo;
      this.isDefault = isDefault;
      this.createDate = createDate;
      this.createUser = createUser;
      this.lastUpdateDate = lastUpdateDate;
      this.lastUpdateUser = lastUpdateUser;
      this.answerType = answerType;
   }

   public AnswerVersion(Long id, Long questionId, String name, Integer orderNo, boolean isDefault,
                        LocalDateTime createDate, String createUser, LocalDateTime lastUpdateDate,
                        String lastUpdateUser, String answerType) {
      this.id = id;
      this.questionId = questionId;
      this.name = name;
      this.orderNo = orderNo;
      this.isDefault = isDefault;
      this.createDate = createDate;
      this.createUser = createUser;
      this.lastUpdateDate = lastUpdateDate;
      this.lastUpdateUser = lastUpdateUser;
      this.answerType = AnswerType.valueOf(answerType);
   }

   public Long getId() {
      return id;
   }

   public void setId(Long id) {
      this.id = id;
   }

   public Long getQuestionId() {
      return questionId;
   }

   public void setQuestionId(Long questionId) {
      this.questionId = questionId;
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

   public AnswerType getAnswerType() {
      return answerType;
   }

   public void setAnswerType(AnswerType answerType) {
      this.answerType = answerType;
   }
}
