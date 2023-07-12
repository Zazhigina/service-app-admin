/*

Разработка-193-Изменение таблицы admin.t_letter_template
https://gitlab.inlinegroup-c.ru/mirror/backend/service-app-admin/-/issues/23#note_78052

*/

ALTER TABLE "admin".t_letter_template ALTER COLUMN type_template SET NOT NULL;
