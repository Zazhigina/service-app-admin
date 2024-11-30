package igc.mirror.feedback.repository;

import igc.mirror.feedback.dto.FeedbackDto;
import igc.mirror.feedback.dto.FeedbackFileDto;
import igc.mirror.feedback.dto.FeedbackReportDto;
import igc.mirror.feedback.dto.FeedbackThemeDto;
import jooqdata.tables.TFeedback;
import jooqdata.tables.TFeedbackFile;
import jooqdata.tables.TFeedbackThemes;
import org.jooq.*;
import org.jooq.impl.DSL;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Repository
public class FeedbackRepository {

    private static final TFeedbackThemes T_FEEDBACK_THEMES = TFeedbackThemes.T_FEEDBACK_THEMES;
    private static final TFeedback T_FEEDBACK = TFeedback.T_FEEDBACK;
    private static final TFeedbackFile T_FEEDBACK_FILE = TFeedbackFile.T_FEEDBACK_FILE;

    private final DSLContext dsl;

    public FeedbackRepository(DSLContext dsl) {
        this.dsl = dsl;
    }

    public List<FeedbackThemeDto> findThemes() {
        return dsl.selectFrom(T_FEEDBACK_THEMES)
                .fetchInto(FeedbackThemeDto.class);
    }
    public FeedbackThemeDto addTheme(FeedbackThemeDto theme) {
        return dsl.insertInto(T_FEEDBACK_THEMES)
                .set(T_FEEDBACK_THEMES.FB_THEME_NAME, theme.getFbThemeName())
                .set(T_FEEDBACK_THEMES.CREATE_USER, theme.getCreateUser())
                .returning()
                .fetchOneInto(FeedbackThemeDto.class);
    }

    public FeedbackThemeDto updateThemeById(Long id, FeedbackThemeDto theme) {
        dsl.update(T_FEEDBACK_THEMES)
                .set(T_FEEDBACK_THEMES.FB_THEME_NAME, theme.getFbThemeName())
                .set(T_FEEDBACK_THEMES.LAST_UPDATE_USER, theme.getLastUpdateUser())
                .where(T_FEEDBACK_THEMES.ID.equal(id))
                .execute();
        return getThemeById(id);
    }
    public FeedbackThemeDto getThemeById(Long id) {
        return dsl.selectFrom(T_FEEDBACK_THEMES)
                .where(T_FEEDBACK_THEMES.ID.equal(id))
                .fetchOneInto(FeedbackThemeDto.class);
    }

    public void deleteThemeById(Long id) {
        dsl.deleteFrom(T_FEEDBACK_THEMES)
                .where(T_FEEDBACK_THEMES.ID.equal(id))
                .execute();
    }

    public FeedbackDto addFeedback(FeedbackDto feedback) {
        return dsl.insertInto(T_FEEDBACK,
                    T_FEEDBACK.FB_THEME_NAME,
                    T_FEEDBACK.FEEDBACK_TEXT,
                    T_FEEDBACK.CREATE_USER,
                    T_FEEDBACK.USER_FULLNAME
                )
                .values(feedback.getFbThemeName(),
                        feedback.getFeedbackText(),
                        feedback.getCreateUser(),
                        feedback.getUserFullname()
                )
                .returning()
                .fetchOneInto(FeedbackDto.class);
    }

    public void addFeedbackFile(FeedbackFileDto feedbackFile) {
        dsl.insertInto(T_FEEDBACK_FILE,
                        T_FEEDBACK_FILE.FEEDBACK_ID,
                        T_FEEDBACK_FILE.DOCUMENT_ID,
                        T_FEEDBACK_FILE.FILENAME,
                        T_FEEDBACK_FILE.CREATE_USER
                )
                .values(feedbackFile.getFeedbackId(),
                        feedbackFile.getDocumentId(),
                        feedbackFile.getFilename(),
                        feedbackFile.getCreateUser()
                )
                .execute();
    }

    public FeedbackFileDto getFeedBackFile(UUID uid) {
        return dsl.selectFrom(T_FEEDBACK_FILE)
                .where(T_FEEDBACK_FILE.UID.eq(uid))
                .fetchOneInto(FeedbackFileDto.class);
    }

    public List<FeedbackReportDto> getReportData(LocalDateTime date1, LocalDateTime date2) {
        Condition dateCondition = DSL.trueCondition();
        if (date1 != null) {
            dateCondition = dateCondition.and(T_FEEDBACK.CREATE_DATE.ge(date1));
        }
        if (date2 != null) {
            dateCondition = dateCondition.and(T_FEEDBACK.CREATE_DATE.le(date2));
        }
        return dsl.select(
                        T_FEEDBACK.ID,
                        T_FEEDBACK.FB_THEME_NAME,
                        T_FEEDBACK.CREATE_DATE,
                        T_FEEDBACK.USER_FULLNAME,
                        T_FEEDBACK.FEEDBACK_TEXT,
                        T_FEEDBACK_FILE.FILENAME,
                        T_FEEDBACK_FILE.UID
                )
                .from(T_FEEDBACK)
                .leftJoin(T_FEEDBACK_FILE)
                .on(T_FEEDBACK.ID.eq(T_FEEDBACK_FILE.FEEDBACK_ID))
                .where(dateCondition)
                .fetchInto(FeedbackReportDto.class);
    }
}