/*

Разработка 325 Создание таблицы маппинга
https://gitlab.inlinegroup-c.ru/mirror/backend/service-app-admin/-/issues/38

*/

ALTER TABLE "admin".t_service_offer_type ALTER COLUMN service_code TYPE varchar USING service_code::varchar;
