package igc.mirror.nsi.model;

import java.time.LocalDateTime;

public interface ClassifierData {
    String getCode();
    String getName();
    LocalDateTime getStartDate();
    LocalDateTime getEndDate();
}
