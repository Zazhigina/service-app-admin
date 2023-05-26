package igc.mirror.model;

import jooqdata.tables.pojos.TLetterTemplate;

import java.time.LocalDateTime;

public class LetterTemplate extends TLetterTemplate {
    public LetterTemplate() {
    }

    public LetterTemplate(Long id, String letterType, String title, Long letterSample, LocalDateTime createDate, String createUser, LocalDateTime lastUpdateDate, String lastUpdateUser, String typeTemplate, String acceptableDocumentFormat) {
        super(id, letterType, title, letterSample, createDate, createUser, lastUpdateDate, lastUpdateUser, typeTemplate, acceptableDocumentFormat);
    }
}
