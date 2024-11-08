/*
Таблица для хранения данных по Обратной связи
*/
-- Table: admin.t_feedback_themes

-- DROP TABLE IF EXISTS admin.t_feedback_themes;

CREATE TABLE IF NOT EXISTS admin.t_feedback
(
    id integer NOT NULL DEFAULT admin.get_id(), -- ID
    fb_theme_name character varying(255) COLLATE pg_catalog."default" NOT NULL,
    feedback_text character varying(1024) COLLATE pg_catalog."default",
    file_attachment character varying(2048) COLLATE pg_catalog."default",
    create_date timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    create_user character varying(100) COLLATE pg_catalog."default",
    CONSTRAINT t_feedback_themes_pkey PRIMARY KEY (id)
);

COMMENT ON TABLE "admin".t_feedback
    IS 'Обратная связь';

COMMENT ON COLUMN "admin".t_feedback.id
    IS 'ID';

COMMENT ON COLUMN "admin".t_feedback.fb_theme_name
    IS 'Имя темы';

COMMENT ON COLUMN "admin".t_feedback.feedback_text
    IS 'Текст обращения';

COMMENT ON COLUMN "admin".t_feedback.create_user
    IS 'Автор обращения';

COMMENT ON COLUMN "admin".t_feedback.file_attachment
    IS 'Файл';

COMMENT ON COLUMN "admin".t_feedback.create_date
    IS 'Дата создания обращения';

-- Trigger: tr_before_row

-- DROP TRIGGER IF EXISTS tr_before_row ON admin.t_feedback;

CREATE OR REPLACE TRIGGER tr_before_row
    BEFORE INSERT OR UPDATE
    ON admin.t_feedback
    FOR EACH ROW
    EXECUTE FUNCTION admin.fn_before_row();
