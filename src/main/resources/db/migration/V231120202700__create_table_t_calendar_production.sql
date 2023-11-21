/*

Разработка-554-Администрирование. Бэк. Затратный метод. Заполнение КП поставщиком
https://gitlab.inlinegroup-c.ru/mirror/backend/service-app-admin/-/issues/48
*/

-- "admin".t_calendar_production

CREATE TABLE "admin".t_calendar_production (
	id int8 NOT NULL DEFAULT admin.get_id(),
	year int4 NOT NULL,
	hour_work_count numeric NULL,
    month_work_hour_count numeric NULL,
	create_date timestamp NULL,
	create_user varchar(100) NULL,
	last_update_date timestamp NULL,
	last_update_user varchar(100) NULL
);
COMMENT ON TABLE "admin".t_calendar_production IS 'Производственный календарь';

-- Column comments

COMMENT ON COLUMN "admin".t_calendar_production.id IS 'Первичный ключ';
COMMENT ON COLUMN "admin".t_calendar_production.year IS 'Год';
COMMENT ON COLUMN "admin".t_calendar_production.hour_work_count IS 'Рабочее время (в часах)';
COMMENT ON COLUMN "admin".t_calendar_production.month_work_hour_count IS 'Количество часов работы в месяц';
COMMENT ON COLUMN "admin".t_calendar_production.create_date IS 'Дата создания';
COMMENT ON COLUMN "admin".t_calendar_production.create_user IS 'Автор создания';
COMMENT ON COLUMN "admin".t_calendar_production.last_update_date IS 'Дата и время изменения';
COMMENT ON COLUMN "admin".t_calendar_production.last_update_user IS 'Автор изменения';

-- Constraint

ALTER TABLE "admin".t_calendar_production ADD CONSTRAINT t_calendar_production_pk PRIMARY KEY (id);
ALTER TABLE "admin".t_calendar_production ADD CONSTRAINT t_calendar_production_un UNIQUE (year);

-- Trigger

CREATE TRIGGER tr_before_row BEFORE
INSERT
    OR
UPDATE
    ON
    admin.t_calendar_production FOR EACH ROW EXECUTE FUNCTION admin.fn_before_row();