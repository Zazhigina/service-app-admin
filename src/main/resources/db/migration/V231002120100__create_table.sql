/*

Разработка 325 Создание таблицы маппинга
https://gitlab.inlinegroup-c.ru/mirror/backend/service-app-admin/-/issues/38

*/

-- "admin".t_service_offer_type

CREATE TABLE "admin".t_service_offer_type (
	id int8 NOT NULL DEFAULT admin.get_id(),
	service_code int8 NOT NULL,
	offer_type varchar NULL,
	create_date timestamp NULL,
	create_user varchar(100) NULL,
	last_update_date timestamp NULL,
	last_update_user varchar(100) NULL
);
COMMENT ON TABLE "admin".t_service_offer_type IS 'Вид шаблона КП для услуги';

-- Column comments

COMMENT ON COLUMN "admin".t_service_offer_type.id IS 'Первичный ключ';
COMMENT ON COLUMN "admin".t_service_offer_type.service_code IS 'Код услуги';
COMMENT ON COLUMN "admin".t_service_offer_type.offer_type IS 'Вид шаблона КП';
COMMENT ON COLUMN "admin".t_service_offer_type.create_date IS 'Дата создания';
COMMENT ON COLUMN "admin".t_service_offer_type.create_user IS 'Автор создания';
COMMENT ON COLUMN "admin".t_service_offer_type.last_update_date IS 'Дата и время изменения';
COMMENT ON COLUMN "admin".t_service_offer_type.last_update_user IS 'Автор изменения';

-- Constraint

ALTER TABLE "admin".t_service_offer_type ADD CONSTRAINT t_service_offer_type_pk PRIMARY KEY (id);

-- Index

CREATE UNIQUE INDEX t_service_offer_type_service_code_idx ON "admin".t_service_offer_type (service_code);

-- Trigger

CREATE TRIGGER tr_before_row BEFORE
INSERT
    OR
UPDATE
    ON
    admin.t_service_offer_type FOR EACH ROW EXECUTE FUNCTION admin.fn_before_row();