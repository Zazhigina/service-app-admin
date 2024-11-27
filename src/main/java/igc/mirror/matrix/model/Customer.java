package igc.mirror.matrix.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class Customer {

    /**
     * Код заказчика
     **/
    private String code;

    /**
     * Наименование заказчика
     **/
    private String name;

    public Customer() {}

}
