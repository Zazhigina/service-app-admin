/*
Разработка и очистка таблиц для обратной связи
https://gitlab.inlinegroup-c.ru/mirror/backend/service-app-admin/-/issues/164
*/

/*
t_feedback_themes
*/
ALTER TABLE admin.t_feedback_themes ALTER COLUMN id TYPE bigint;
ALTER TABLE admin.t_feedback_themes DROP COLUMN IF EXISTS unique_id;
ALTER TABLE admin.t_feedback_themes ADD last_update_user character varying(100);
COMMENT ON COLUMN admin.t_feedback_themes.last_update_user IS 'Автор изменения';

/*
t_feedback
*/
ALTER TABLE admin.t_feedback ALTER COLUMN id TYPE bigint;
ALTER TABLE admin.t_feedback ALTER COLUMN feedback_text TYPE text COLLATE pg_catalog."default";
ALTER TABLE admin.t_feedback ADD last_update_user character varying(100);
COMMENT ON COLUMN admin.t_feedback.last_update_user IS 'Автор изменения';

/*
t_feedback_file_store
*/
DROP TABLE IF EXISTS admin.t_feedback_file_store;

/*
t_feedback_file
*/
CREATE TABLE admin.t_feedback_file
(
    uid uuid DEFAULT gen_random_uuid(),
    feedback_id bigint NOT NULL,
    document_id bigint NOT NULL,
    filename text NOT NULL,
	create_date timestamp without time zone,
    create_user character varying(100) COLLATE pg_catalog."default",
    last_update_date timestamp without time zone,
    last_update_user character varying(100) COLLATE pg_catalog."default",
    PRIMARY KEY (uid)
);

ALTER TABLE IF EXISTS admin.t_feedback_file
    OWNER to mirror;
	
COMMENT ON TABLE admin.t_feedback_file IS 'Файлы обратной связи';
COMMENT ON COLUMN admin.t_feedback_file.uid IS 'UID';
COMMENT ON COLUMN admin.t_feedback_file.feedback_id IS 'ID обращения';
COMMENT ON COLUMN admin.t_feedback_file.document_id IS 'ID документа в сервисе документов';
COMMENT ON COLUMN admin.t_feedback_file.filename IS 'Название файла';
COMMENT ON COLUMN admin.t_feedback_file.create_date IS 'Дата и время создания';
COMMENT ON COLUMN admin.t_feedback_file.create_user IS 'Автор создания';
COMMENT ON COLUMN admin.t_feedback_file.last_update_date IS 'Дата и время изменения';
COMMENT ON COLUMN admin.t_feedback_file.last_update_user IS 'Автор изменения';

CREATE TRIGGER tr_before_row
    BEFORE INSERT OR UPDATE 
    ON admin.t_feedback_file
    FOR EACH ROW
    EXECUTE FUNCTION admin.fn_before_row();