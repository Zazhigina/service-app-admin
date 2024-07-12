package igc.mirror.neuro.ref;

import lombok.Getter;

@Getter
public enum ConfigType {
    ACTIVE("active"),
    LATEST("latest"),
    ALL("all");

    private final String name;

    ConfigType(String name) {
        this.name = name;
    }
}
