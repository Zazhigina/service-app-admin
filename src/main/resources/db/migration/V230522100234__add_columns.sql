-- Разработка-193-Создание таблицы

--Казак Татьяна
-- Необходимо внести изменения в таблицу admin.t_letter_template:
-- 1. Переименовать название поля /Заголовок письма/ на /Заголовок/, с letter_title на title
-- 2. Добавить новое поле: Тех.имя - type_template Имя - Вид шаблона Тип данных - varchar 
--    Значения для поля - Документ или Шаблон
-- 3. Добавить новое поле: Тех.имя - acceptable_document_format Имя - Допустимый формат документа Тип данных - varchar 
--    Значения для поля - doc, docx, html

ALTER TABLE "admin".t_letter_template RENAME COLUMN letter_title TO title;
COMMENT ON COLUMN "admin".t_letter_template.title IS 'Заголовок';

ALTER TABLE "admin".t_letter_template ADD type_template varchar NULL;
COMMENT ON COLUMN "admin".t_letter_template.type_template IS 'Вид шаблона. Enum: Документ, Шаблон';

ALTER TABLE "admin".t_letter_template ADD acceptable_document_format varchar NULL;
COMMENT ON COLUMN "admin".t_letter_template.acceptable_document_format IS 'Допустимый формат документа. Enum: doc, docx, html';

-- "admin".t_letter_template_acceptable_document_format_enum definition

-- Drop table

-- DROP TABLE "admin".t_letter_template_acceptable_document_format_enum;

CREATE TABLE "admin".t_letter_template_acceptable_document_format_enum (
	id int8 NOT NULL, -- ID
	"name" varchar NULL, -- Наименование
	description varchar NULL, -- Описание
	CONSTRAINT t_letter_template_acceptable_document_format_enum_pk PRIMARY KEY (id),
	CONSTRAINT t_letter_template_acceptable_document_format_enum_un UNIQUE (name)
);
COMMENT ON TABLE "admin".t_letter_template_acceptable_document_format_enum IS 'Допустимый формат документа (Enum)';

-- Column comments

COMMENT ON COLUMN "admin".t_letter_template_acceptable_document_format_enum.id IS 'ID';
COMMENT ON COLUMN "admin".t_letter_template_acceptable_document_format_enum."name" IS 'Наименование';
COMMENT ON COLUMN "admin".t_letter_template_acceptable_document_format_enum.description IS 'Описание';


-- "admin".t_letter_template_type_template_enum definition

-- Drop table

-- DROP TABLE "admin".t_letter_template_type_template_enum;

CREATE TABLE "admin".t_letter_template_type_template_enum (
	id int8 NOT NULL, -- ID
	"name" varchar NULL, -- Наименование
	description varchar NULL, -- Описание
	CONSTRAINT t_letter_template_type_template_enum_pk PRIMARY KEY (id),
	CONSTRAINT t_letter_template_type_template_enum_un UNIQUE (name)
);
COMMENT ON TABLE "admin".t_letter_template_type_template_enum IS 'Вид шаблона (Enum)';

-- Column comments

COMMENT ON COLUMN "admin".t_letter_template_type_template_enum.id IS 'ID';
COMMENT ON COLUMN "admin".t_letter_template_type_template_enum."name" IS 'Наименование';
COMMENT ON COLUMN "admin".t_letter_template_type_template_enum.description IS 'Описание';

ALTER TABLE "admin".t_letter_template ADD CONSTRAINT t_letter_template_fk_1 FOREIGN KEY (type_template) REFERENCES "admin".t_letter_template_type_template_enum("name");
ALTER TABLE "admin".t_letter_template ADD CONSTRAINT t_letter_template_fk_2 FOREIGN KEY (acceptable_document_format) REFERENCES "admin".t_letter_template_acceptable_document_format_enum("name");

INSERT INTO "admin".t_letter_template_type_template_enum (id, "name", description) VALUES(1, 'Документ', NULL);
INSERT INTO "admin".t_letter_template_type_template_enum (id, "name", description) VALUES(2, 'Шаблон', NULL);

INSERT INTO "admin".t_letter_template_acceptable_document_format_enum (id, "name", description) VALUES(1, 'doc', NULL);
INSERT INTO "admin".t_letter_template_acceptable_document_format_enum (id, "name", description) VALUES(2, 'docx', NULL);
INSERT INTO "admin".t_letter_template_acceptable_document_format_enum (id, "name", description) VALUES(3, 'html', NULL);
