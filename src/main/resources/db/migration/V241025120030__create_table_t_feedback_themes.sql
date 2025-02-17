/*
Справочник тем для обратной связи
*/
-- Table: admin.t_feedback_themes

-- DROP TABLE IF EXISTS admin.t_feedback_themes;

CREATE TABLE IF NOT EXISTS admin.t_feedback_themes
(
    id integer NOT NULL DEFAULT admin.get_id(), -- ID
    fb_theme_name character varying(255) COLLATE pg_catalog."default" NOT NULL,
    create_date timestamp without time zone,
    last_update_date timestamp without time zone,
    create_user character varying(100) COLLATE pg_catalog."default"
);

COMMENT ON TABLE "admin".t_feedback_themes
    IS 'Справочник тем для обратной связи';

COMMENT ON COLUMN "admin".t_feedback_themes.id
    IS 'ID';

COMMENT ON COLUMN "admin".t_feedback_themes.fb_theme_name
    IS 'Имя темы';

COMMENT ON COLUMN "admin".t_feedback_themes.create_date
    IS 'Дата создания темы';

COMMENT ON COLUMN "admin".t_feedback_themes.last_update_date
    IS 'Дата изменения темы';

COMMENT ON COLUMN "admin".t_feedback_themes.create_user
    IS 'Автор создавший тему';

 -- Table Triggers

CREATE TRIGGER tr_before_row BEFORE
INSERT
    OR
UPDATE
    ON
    admin.t_feedback_themes FOR EACH ROW EXECUTE FUNCTION admin.fn_before_row();

-- insert into t_feedback_themes
 INSERT INTO "admin".t_feedback_themes (fb_theme_name, create_user) VALUES
            ('Вопрос по работе сервисов Effect', 'system'),
            ('Запрос на сопровождение закупки в Effect', 'system'),
            ('На рабочем столе не отображается ярлык Effect', 'system'),
            ('Предложения по улучшению сервисов Effect', 'system'),
            ('Сообщить о неисправности', 'system'),
            ('Другая тема', 'system');