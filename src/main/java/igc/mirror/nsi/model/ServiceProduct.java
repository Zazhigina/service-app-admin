package igc.mirror.nsi.model;

import java.time.LocalDateTime;

public class ServiceProduct implements ClassifierData{
    /** Код услуги **/
    private String code;

    /** Наименование услуги **/

    private String name;

    /** Наименование услуги на английском **/

    private String nameEn;

    /** Начало скрока действия записи */
    private LocalDateTime startDate;

    /** Окончание срока действия записи */
    private LocalDateTime endDate;

    /** Класс услуги **/
    private Long classId;

    /** Тип услуги **/
    private  Long typeId;

    public ServiceProduct(){}


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNameEn() {
        return nameEn;
    }

    public void setNameEn(String nameEn) {
        this.nameEn = nameEn;
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

    public Long getClassId() {
        return classId;
    }

    public void setClassId(Long classId) {
        this.classId = classId;
    }

    public Long getTypeId() {
        return typeId;
    }

    public void setTypeId(Long typeId) {
        this.typeId = typeId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return "ServiceProduct{" +
                "code='" + code + '\'' +
                ", name='" + name + '\'' +
                ", nameEn='" + nameEn + '\'' +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", classId=" + classId +
                ", typeId=" + typeId +
                '}';
    }
}
