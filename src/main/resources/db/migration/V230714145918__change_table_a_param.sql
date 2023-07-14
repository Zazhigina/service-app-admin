/*

Разработка-198-Добавление новых параметров в таблицу
https://gitlab.inlinegroup-c.ru/mirror/model/-/issues/116

*/

-- Расширяем колонки, т.к. требуемые данные не помещаются
ALTER TABLE "admin".a_param ALTER COLUMN "key" TYPE varchar USING "key"::varchar;
ALTER TABLE "admin".a_param ALTER COLUMN "name" TYPE varchar USING "name"::varchar;
ALTER TABLE "admin".a_param ALTER COLUMN val TYPE varchar USING val::varchar;
ALTER TABLE "admin".a_param ALTER COLUMN default_val TYPE varchar USING default_val::varchar;

-- Изменяем параметр
UPDATE "admin".a_param ap SET "name" = 'Шаблон URL для генерации ссылки доступа поставщика на КП' WHERE "key" = 'URL_FOR_COMPANY.LINK';
UPDATE "admin".a_param ap SET "val" = 'https://mirror-partner-ep.inlinegroup-c.ru/filling-kp/' WHERE "key" = 'URL_FOR_COMPANY.LINK';
UPDATE "admin".a_param ap SET "key" = 'URL_FOR_COMPANY.EP' WHERE "key" = 'URL_FOR_COMPANY.LINK';

-- Добавляем новые параметры
INSERT INTO "admin".a_param ("key", "name", val, create_date, create_user, last_update_date, last_update_user, default_val) VALUES('URL_FOR_COMPANY.MA', 'Шаблон URL для генерации ссылки доступа поставщика МА', NULL, '2023-07-14 15:23:57.151', NULL, NULL, NULL, NULL);
INSERT INTO "admin".a_param ("key", "name", val, create_date, create_user, last_update_date, last_update_user, default_val) VALUES('REPLY_MESSAGE.URL_EP', 'Шаблон URL для генерации ссылки доступа инициатора в запрос ЕР', 'https://mirror-ep.inlinegroup-c.ru/calc-request/', '2023-07-14 15:23:57.151', NULL, NULL, NULL, NULL);
INSERT INTO "admin".a_param ("key", "name", val, create_date, create_user, last_update_date, last_update_user, default_val) VALUES('REPLY_MESSAGE.URL_MA', 'Шаблон URL для генерации ссылки доступа инициатора в запрос МА', 'https://mirror-ma.inlinegroup-c.ru/market-assist/', '2023-07-14 15:23:57.151', NULL, NULL, NULL, NULL);
