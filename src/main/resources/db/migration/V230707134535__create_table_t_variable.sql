/*

Разработка-193-Изменение структры БД для хранения переменных шаблона
https://gitlab.inlinegroup-c.ru/mirror/backend/service-app-admin/-/issues/21

*/

CREATE TABLE "admin".t_variable (
	id int8 NOT NULL DEFAULT admin.get_id(), -- Первичный ключ
	"name" varchar NULL, -- Наименование
	description varchar NULL, -- Описание
	default_val varchar NULL, -- Значение по умолчанию
	create_date timestamp NULL, -- Дата и время создания
	create_user varchar(100) NULL, -- Автор создания
	last_update_date timestamp NULL, -- Дата и время изменения
	last_update_user varchar(100) NULL, -- Автор изменения
	CONSTRAINT t_variable_pk PRIMARY KEY (id)
);
COMMENT ON TABLE "admin".t_variable IS 'Переменная';

-- Column comments

COMMENT ON COLUMN "admin".t_variable.id IS 'Первичный ключ';
COMMENT ON COLUMN "admin".t_variable."name" IS 'Наименование';
COMMENT ON COLUMN "admin".t_variable.description IS 'Описание';
COMMENT ON COLUMN "admin".t_variable.default_val IS 'Значение по умолчанию';
COMMENT ON COLUMN "admin".t_variable.create_date IS 'Дата и время создания';
COMMENT ON COLUMN "admin".t_variable.create_user IS 'Автор создания';
COMMENT ON COLUMN "admin".t_variable.last_update_date IS 'Дата и время изменения';
COMMENT ON COLUMN "admin".t_variable.last_update_user IS 'Автор изменения';

-- Table Triggers

CREATE TRIGGER tr_before_row BEFORE
INSERT
    OR
UPDATE
    ON
    admin.t_variable FOR EACH ROW EXECUTE FUNCTION admin.fn_before_row();



ALTER TABLE "admin".t_letter_template_variable ADD variable_id int8 NULL;
ALTER TABLE "admin".t_letter_template_variable ADD CONSTRAINT t_letter_template_variable_fk2 FOREIGN KEY (variable_id) REFERENCES "admin".t_variable(id);



COMMENT ON COLUMN "admin".t_letter_template_variable.variable_id IS 'Переменная';
COMMENT ON COLUMN "admin".t_letter_template_variable."name" IS 'Переменная. Удалить!!!';
COMMENT ON COLUMN "admin".t_letter_template_variable.val IS 'Наименование переменной. Удалить!!!';



ALTER TABLE "admin".t_letter_template_variable RENAME CONSTRAINT t_letter_template_variables_fk TO t_letter_template_variable_fk1;
