package igc.mirror.matrix.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;

public class Initiator {

    /**
     * Код инициатора
     **/
    private String initiatorCode;

    /**
     * Наименование инициатора
     **/
    private String initiatorName;

    /**
     * Наименование блока
     */
    private String initiatorBlock;

    /**
     * Код заказчика
     */
    private String customerCode;

    /**
     * Флаг актуальности
     */
    @JsonProperty("isNotRelevant")
    private boolean isNotRelevant;

    /**
     * Начало срока действия записи
     */
    private LocalDateTime startDate;

    /**
     * Окончание срока действия записи
     */
    private LocalDateTime endDate;

    public Initiator(){}

    public String getInitiatorCode() {
        return initiatorCode;
    }

    public void setCode(String initiatorCode) {
        this.initiatorCode = initiatorCode;
    }

    public String getInitiatorName() {
        return initiatorName;
    }

    public void setName(String initiatorName) {
        this.initiatorName = initiatorName;
    }

    public String getInitiatorBlock() {
        return initiatorBlock;
    }

    public void setBlock(String initiatorBlock) {
        this.initiatorBlock = initiatorBlock;
    }

    public String getCustomerCode() {
        return customerCode;
    }

    public void setCustomerCode(String customerCode) {
        this.customerCode = customerCode;
    }

    public boolean isNotRelevant() {
        return isNotRelevant;
    }

    public void setNotRelevant(boolean notRelevant) {
        isNotRelevant = notRelevant;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    @Override
    public String toString() {
        return "Initiator{" +
                "code='" + initiatorCode + '\'' +
                ", name='" + initiatorName + '\'' +
                ", block='" + initiatorBlock + '\'' +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", customerCode=" + customerCode +
                '}';
    }
}
