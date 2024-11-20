package igc.mirror.exchange.dto;

import igc.mirror.exchange.model.ExternalSource;

public record ExternalSourceDto(Long id, String code, String name, String description, boolean deleted) {
    public ExternalSource toModel() {
        return new ExternalSource(this);
    }

     public ExternalSourceDto(ExternalSource model) {
        this(
                model.getId(),
                model.getCode(),
                model.getName(),
                model.getDescription(),
                model.isDeleted()
        );
    }
}
