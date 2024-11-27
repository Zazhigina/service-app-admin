/*
Разработка и очистка таблиц для обратной связи
https://gitlab.inlinegroup-c.ru/mirror/backend/service-app-admin/-/issues/164
*/

/*
Удаление лишних столбоцв
*/
ALTER TABLE admin.t_feedback DROP COLUMN IF EXISTS file_attachment;
ALTER TABLE admin.t_feedback DROP COLUMN IF EXISTS file_store;

/*
Добавление столбца ФИО юзера для report feedback
*/
ALTER TABLE admin.t_feedback ADD user_fullname character varying(1024);
COMMENT ON COLUMN "admin".t_feedback.user_fullname IS 'ФИО инициатора';

/*
Справочник тем для обратной связи, добавляем UUID в feedback themes
*/

ALTER TABLE admin.t_feedback_themes ADD unique_id UUID DEFAULT gen_random_uuid();
COMMENT ON COLUMN "admin".t_feedback_themes.unique_id IS 'GUID для темы';


/*
Справочник тем для обратной связи, добавляем UUID в filestore
*/

-- Table: admin.t_feedback_file_store

-- DROP TABLE IF EXISTS admin.t_feedback_file_store;

CREATE TABLE IF NOT EXISTS admin.t_feedback_file_store
(
    id integer NOT NULL DEFAULT admin.get_id(),
    create_date timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    create_user character varying(100) COLLATE pg_catalog."default",
    file_extension character varying(100) COLLATE pg_catalog."default",
    file_name character varying(3000) COLLATE pg_catalog."default",
    file_size bigint,
    last_update_date timestamp without time zone,
    feedback_id integer,
    file_blob BYTEA,
	"UID" UUID DEFAULT gen_random_uuid(),
    CONSTRAINT t_feedback_file_store_pkey PRIMARY KEY (id),
    CONSTRAINT fk_feedback_fkey FOREIGN KEY (feedback_id)
        REFERENCES admin.t_feedback (id) MATCH SIMPLE
);

COMMENT ON TABLE "admin".t_feedback_file_store
    IS 'Таблица для хранения вложений ОС';

COMMENT ON COLUMN "admin".t_feedback_file_store.id
    IS 'ID';

COMMENT ON COLUMN "admin".t_feedback_file_store.create_date
    IS 'Дата создания';

COMMENT ON COLUMN "admin".t_feedback_file_store.create_user
    IS 'Создатель';

COMMENT ON COLUMN "admin".t_feedback_file_store.file_extension
    IS 'Расширение';

COMMENT ON COLUMN "admin".t_feedback_file_store.file_name
    IS 'Название файла';

COMMENT ON COLUMN "admin".t_feedback_file_store.file_size
    IS 'Размер файла';

COMMENT ON COLUMN "admin".t_feedback_file_store.last_update_date
    IS 'Последнее обновление';

COMMENT ON COLUMN "admin".t_feedback_file_store.file_blob
    IS 'Файл';

COMMENT ON COLUMN "admin".t_feedback_file_store."UID" 
	IS 'GUID для файла';

-- Trigger: tr_before_row

-- DROP TRIGGER IF EXISTS tr_before_row ON admin.t_feedback_file_store;

CREATE OR REPLACE TRIGGER tr_before_row
    BEFORE INSERT OR UPDATE
    ON admin.t_feedback_file_store
    FOR EACH ROW
    EXECUTE FUNCTION admin.fn_before_row();