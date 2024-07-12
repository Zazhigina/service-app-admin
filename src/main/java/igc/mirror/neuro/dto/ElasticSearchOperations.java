package igc.mirror.neuro.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ElasticSearchOperations {

    private Boolean acknowledged;
    private String log;

    public ElasticSearchOperations(Boolean acknowledged, String log) {
        this.acknowledged = acknowledged;
        this.log = log;
    }
}
