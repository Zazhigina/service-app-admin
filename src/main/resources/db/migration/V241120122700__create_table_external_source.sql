/*
  Таблица для ведения систем-источников
  https://gitlab.inlinegroup-c.ru/mirror/backend/service-app-admin/-/issues/172
*/

-- "admin".t_external_source определение

-- Drop table

-- DROP TABLE "admin".t_external_source;

CREATE TABLE "admin".t_external_source (
	id int8 DEFAULT admin.get_id() NOT NULL, -- ID
	code varchar NULL, -- Код
	"name" varchar NULL, -- Наименование
	description varchar NULL, -- Описание
	deleted bool NULL, -- Признак удаления
	create_date timestamp NULL, -- Дата и время создания
	create_user varchar(100) NULL, -- Автор создания
	last_update_date timestamp NULL, -- Дата и время изменения
	last_update_user varchar(100) NULL, -- Автор изменения
	CONSTRAINT t_external_source_pk PRIMARY KEY (id)
);
CREATE UNIQUE INDEX t_external_source_code_idx ON admin.t_external_source USING btree (code);
COMMENT ON TABLE "admin".t_external_source IS 'Системы-источники';

-- Column comments

COMMENT ON COLUMN "admin".t_external_source.id IS 'ID';
COMMENT ON COLUMN "admin".t_external_source.code IS 'Код';
COMMENT ON COLUMN "admin".t_external_source."name" IS 'Наименование';
COMMENT ON COLUMN "admin".t_external_source.description IS 'Описание';
COMMENT ON COLUMN "admin".t_external_source.deleted IS 'Признак удаления';
COMMENT ON COLUMN "admin".t_external_source.create_date IS 'Дата и время создания';
COMMENT ON COLUMN "admin".t_external_source.create_user IS 'Автор создания';
COMMENT ON COLUMN "admin".t_external_source.last_update_date IS 'Дата и время изменения';
COMMENT ON COLUMN "admin".t_external_source.last_update_user IS 'Автор изменения';

-- Table Triggers

create trigger tr_before_row before
insert
    or
update
    on
    admin.t_external_source for each row execute function admin.fn_before_row();