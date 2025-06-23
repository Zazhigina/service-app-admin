/*
Разработка обратной связи. Добавление поля для сортировки тем
https://gitlab.inlinegroup-c.ru/mirror/backend/service-app-admin/-/issues/164
*/

ALTER TABLE IF EXISTS admin.t_feedback_themes ADD COLUMN num integer;
COMMENT ON COLUMN "admin".t_feedback_themes.num IS 'Номер для сортировки';