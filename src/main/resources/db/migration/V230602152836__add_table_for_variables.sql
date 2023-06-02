/*

Разработка-193-Создание таблицы t_letter_template_variables в репозитории ADMIN
https://gitlab.inlinegroup-c.ru/mirror/model/-/issues/103

*/


-- "admin".t_letter_template_variables definition

-- Drop table

-- DROP TABLE "admin".t_letter_template_variables;

CREATE TABLE "admin".t_letter_template_variables (
	id int8 NOT NULL DEFAULT admin.get_id(), -- Первичный ключ
	letter_template_id int8 NOT NULL, -- Шаблон письма
	variable varchar NULL, -- Переменная
	variable_name varchar NULL, -- Наименование переменной
	create_date timestamp NULL, -- Дата и время создания
	create_user varchar(100) NULL, -- Автор создания
	last_update_date timestamp NULL, -- Дата и время изменения
	last_update_user varchar(100) NULL, -- Автор изменения
	CONSTRAINT t_letter_template_variables_pk PRIMARY KEY (id)
);
COMMENT ON TABLE "admin".t_letter_template_variables IS 'Переменные к шаблону письма';

-- Column comments

COMMENT ON COLUMN "admin".t_letter_template_variables.id IS 'Первичный ключ';
COMMENT ON COLUMN "admin".t_letter_template_variables.letter_template_id IS 'Шаблон письма';
COMMENT ON COLUMN "admin".t_letter_template_variables.variable IS 'Переменная';
COMMENT ON COLUMN "admin".t_letter_template_variables.variable_name IS 'Наименование переменной';
COMMENT ON COLUMN "admin".t_letter_template_variables.create_date IS 'Дата и время создания';
COMMENT ON COLUMN "admin".t_letter_template_variables.create_user IS 'Автор создания';
COMMENT ON COLUMN "admin".t_letter_template_variables.last_update_date IS 'Дата и время изменения';
COMMENT ON COLUMN "admin".t_letter_template_variables.last_update_user IS 'Автор изменения';

-- Table Triggers

CREATE TRIGGER tr_before_row BEFORE
INSERT
    OR
UPDATE
    ON
    admin.t_letter_template_variables FOR EACH ROW EXECUTE FUNCTION ep.fn_before_row();


-- "admin".t_letter_template_variables foreign keys

ALTER TABLE "admin".t_letter_template_variables ADD CONSTRAINT t_letter_template_variables_fk FOREIGN KEY (letter_template_id) REFERENCES "admin".t_letter_template(id);
