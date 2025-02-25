/*
    Task-096-Реализация ведения нормативных сроков расчета по классам МТР (п.14-18, р.2)
    https://gitlab.inlinegroup-c.ru/mirror/backend/service-app-admin/-/issues/185
*/

CREATE TABLE "admin".t_calendar_day (
	id int8 DEFAULT admin.get_id() NOT NULL, -- Идентификатор
	"date" date NOT NULL, -- Дата
	working bool NOT NULL, -- Рабочий день?
	create_date timestamp NULL, -- Дата создания
	create_user varchar(100) NULL, -- Автор создания
	last_update_date timestamp NULL, -- Дата и время изменения
	last_update_user varchar(100) NULL, -- Автор изменения
	CONSTRAINT t_calendar_day_pk PRIMARY KEY (id),
	CONSTRAINT t_calendar_day_un UNIQUE (date)
);
COMMENT ON TABLE "admin".t_calendar_day IS 'Календарь рабочих и выходных дней (исключения)';

-- Column comments

COMMENT ON COLUMN "admin".t_calendar_day.id IS 'Идентификатор';
COMMENT ON COLUMN "admin".t_calendar_day."date" IS 'Дата';
COMMENT ON COLUMN "admin".t_calendar_day.working IS 'Рабочий день?';
COMMENT ON COLUMN "admin".t_calendar_day.create_date IS 'Дата создания';
COMMENT ON COLUMN "admin".t_calendar_day.create_user IS 'Автор создания';
COMMENT ON COLUMN "admin".t_calendar_day.last_update_date IS 'Дата и время изменения';
COMMENT ON COLUMN "admin".t_calendar_day.last_update_user IS 'Автор изменения';

-- Table Triggers

create trigger tr_before_row before
insert
    or
update
    on
    admin.t_calendar_day for each row execute function admin.fn_before_row();
