/*
Разработка-856-НСУ.Цифровизация.Администрирование. Backend
https://gitlab.inlinegroup-c.ru/mirror/backend/service-app-admin/-/issues/61
*/

ALTER TABLE "admin".t_service_offer_type ADD with_costing_default bool NOT NULL DEFAULT false;
COMMENT ON COLUMN "admin".t_service_offer_type.with_costing_default IS 'Запросить расшифровку (по умолчанию)';
