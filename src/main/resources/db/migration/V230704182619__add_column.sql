/*

Разработка-192-Переименование поля is_used в таблице admin.t_answer_version
https://gitlab.inlinegroup-c.ru/mirror/backend/service-app-admin/-/issues/20

*/

ALTER TABLE "admin".t_answer_version ADD is_default bool NULL;
COMMENT ON COLUMN "admin".t_answer_version.is_default IS 'Индикатор выбора ответа';
COMMENT ON COLUMN "admin".t_answer_version.is_used IS 'Индикатор выбора ответа. Удалить!!!';
UPDATE "admin".t_answer_version SET is_default = is_used WHERE is_default IS NULL ;
