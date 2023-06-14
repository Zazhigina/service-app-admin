package igc.mirror.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import igc.mirror.model.AnswerVersion;
import jakarta.validation.constraints.NotBlank;

public class AnswerVersionDto {

   /**
    * Вариант ответа
    */
   @NotBlank(message = "Текст варианта ответа не должен быть пустым")
   private String name;

   /**
    * Порядковый номер ответа
    */
   @NotBlank(message = "Порядковый номер варианта ответа не должен быть пустым")
   private Integer orderNo;

   /**
    * Вариант ответа выбран по умолчанию
    */
   @JsonProperty("isUsed")
   private boolean isUsed;

   public AnswerVersionDto() {}

   public AnswerVersionDto(AnswerVersion answerVersion) {
      this.name = answerVersion.getName();
      this.orderNo = answerVersion.getOrderNo();
      this.isUsed = answerVersion.isUsed();
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

   public boolean isUsed() {
      return isUsed;
   }

   public void setUsed(boolean used) {
      isUsed = used;
   }
}
