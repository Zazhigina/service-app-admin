/*
Разработка-872-Управление сервисами(заглушка)
https://gitlab.inlinegroup-c.ru/mirror/backend/service-app-admin/-/issues/72 TODO
*/

-- "admin".t_app_control определение

-- Drop table

-- DROP TABLE "admin".t_app_control;

CREATE TABLE IF NOT EXISTS "admin".t_app_control
(
    name character varying(100) COLLATE pg_catalog."default" NOT NULL,
    description character varying COLLATE pg_catalog."default",
    enabled boolean NOT NULL DEFAULT true,
	create_date timestamp without time zone,
    create_user character varying(100) COLLATE pg_catalog."default",
    last_update_date timestamp without time zone,
    last_update_user character varying(100) COLLATE pg_catalog."default",
    CONSTRAINT t_app_control_pkey PRIMARY KEY (name)
);
COMMENT ON TABLE "admin".t_app_control IS 'Управление сервисами(заглушка)';

-- Column comments

COMMENT ON COLUMN "admin".t_app_control.name IS 'Наименование';
COMMENT ON COLUMN "admin".t_app_control.description IS 'Описание';
COMMENT ON COLUMN "admin".t_app_control.enabled IS '1 работает в штатном режиме, 0 показывать заглушку';

-- Table Triggers

CREATE TRIGGER tr_before_row BEFORE
INSERT
    OR
UPDATE
    ON
    admin.t_app_control FOR EACH ROW EXECUTE FUNCTION admin.fn_before_row();

--insert into t_app_control
INSERT INTO "admin".t_app_control ("name", description) VALUES('ui-ep', 'Клиентская часть функционала, обеспечивающая автоматизированную поддержку функций инициатора/организатора закупок для расчета цены закупки');
INSERT INTO "admin".t_app_control ("name", description) VALUES('ui-ma', 'Клиентская часть функционала, обеспечивающая автоматизированную поддержку функций инициатора/организатора закупок для поиска контрагентов');
INSERT INTO "admin".t_app_control ("name", description) VALUES('ui-userprofile', 'Клиентская часть сервиса профиля пользователя');
INSERT INTO "admin".t_app_control ("name", description) VALUES('ui-chat', 'Клиентская часть функционала, обеспечивающая переписку с поставщиком в формате чата');
INSERT INTO "admin".t_app_control ("name", description) VALUES('ui-bp', 'Клиентская часть функционала, обеспечивающая процесс формирования данных о единичных расценках по итогам завершенных закупок (База цен)');
INSERT INTO "admin".t_app_control ("name", description) VALUES('ui-bar', 'Общая клиентская часть функционала, включающая меню приложения, строку статуса и заголовочную часть');