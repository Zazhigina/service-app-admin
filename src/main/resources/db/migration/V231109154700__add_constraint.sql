/*

Разработка 325 Создание таблицы маппинга
https://gitlab.inlinegroup-c.ru/mirror/backend/service-app-admin/-/issues/38

*/

ALTER TABLE "admin".t_service_offer_type ADD CONSTRAINT t_service_offer_type_un UNIQUE (service_code);

DROP INDEX "admin".t_service_offer_type_service_code_idx;