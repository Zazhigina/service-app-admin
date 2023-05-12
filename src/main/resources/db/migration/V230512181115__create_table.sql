-- "admin".t_letter_template definition

-- Drop table

-- DROP TABLE "admin".t_letter_template;

CREATE TABLE "admin".t_letter_template (
	id int8 NOT NULL DEFAULT admin.get_id(), -- ID
	letter_type varchar NOT NULL, -- Параметр имя
	letter_title varchar NOT NULL, -- Заголовок письма
	letter_sample varchar NOT NULL, -- Документ
	create_date timestamp NULL, -- Дата и время создания
	create_user varchar(100) NULL, -- Автор создания
	last_update_date timestamp NULL, -- Дата и время изменения
	last_update_user varchar(100) NULL, -- Автор изменения
	CONSTRAINT t_letter_template_pk PRIMARY KEY (id)
);
COMMENT ON TABLE "admin".t_letter_template IS 'Шаблон письма';

-- Column comments

COMMENT ON COLUMN "admin".t_letter_template.id IS 'ID';
COMMENT ON COLUMN "admin".t_letter_template.letter_type IS 'Параметр имя';
COMMENT ON COLUMN "admin".t_letter_template.letter_title IS 'Заголовок письма';
COMMENT ON COLUMN "admin".t_letter_template.letter_sample IS 'Документ';
COMMENT ON COLUMN "admin".t_letter_template.create_date IS 'Дата и время создания';
COMMENT ON COLUMN "admin".t_letter_template.create_user IS 'Автор создания';
COMMENT ON COLUMN "admin".t_letter_template.last_update_date IS 'Дата и время изменения';
COMMENT ON COLUMN "admin".t_letter_template.last_update_user IS 'Автор изменения';

-- Table Triggers

CREATE TRIGGER tr_before_row BEFORE
INSERT
    OR
UPDATE
    ON
    admin.t_letter_template FOR EACH ROW EXECUTE FUNCTION admin.fn_before_row();