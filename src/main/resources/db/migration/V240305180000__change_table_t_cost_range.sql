/*
Разработка-872-Таблица стоимостных показателей опыта в виде диапазонов
https://gitlab.inlinegroup-c.ru/mirror/backend/service-app-admin/-/issues/72
*/

-- "admin".t_cost_range определение

-- Drop table

DROP TABLE IF EXISTS "admin".t_cost_range;

CREATE TABLE "admin".t_cost_range (
	id int8 NOT NULL DEFAULT admin.get_id(), -- ID
	lower_bound numeric NOT NULL, -- Нижняя граница
	upper_bound numeric NOT NULL, -- Верхняя граница
	interval_step numeric NOT NULL, -- Шаг Интервала
	range_text varchar NOT NULL, -- Текст диапазона
	create_date timestamp NULL, -- Дата и время создания
	create_user varchar(100) NULL, -- Автор создания
	last_update_date timestamp NULL, -- Дата и время изменения
	last_update_user varchar(100) NULL, -- Автор изменения
	CONSTRAINT t_cost_range_pk PRIMARY KEY (id)
);
COMMENT ON TABLE "admin".t_cost_range IS 'Диапазоны стоимостных показателей';

-- Column comments

COMMENT ON COLUMN "admin".t_cost_range.id IS 'ID';
COMMENT ON COLUMN "admin".t_cost_range.lower_bound IS 'Нижняя граница';
COMMENT ON COLUMN "admin".t_cost_range.upper_bound IS 'Верхняя граница';
COMMENT ON COLUMN "admin".t_cost_range.interval_step IS 'Шаг Интервала ';
COMMENT ON COLUMN "admin".t_cost_range.range_text IS 'Текст диапазона';
COMMENT ON COLUMN "admin".t_cost_range.create_date IS 'Дата и время создания';
COMMENT ON COLUMN "admin".t_cost_range.create_user IS 'Автор создания';
COMMENT ON COLUMN "admin".t_cost_range.last_update_date IS 'Дата и время изменения';
COMMENT ON COLUMN "admin".t_cost_range.last_update_user IS 'Автор изменения';

-- Table Triggers

create trigger tr_before_row before
insert
    or
update
    on
    admin.t_cost_range for each row execute function admin.fn_before_row();