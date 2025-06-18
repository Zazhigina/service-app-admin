package igc.mirror.monitoring.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ServiceData {

    private Long id;
    /**
     * Название сервиса
     */
    private String name;

    /**
     * Описание сервиса
     */
    private String description;

}
