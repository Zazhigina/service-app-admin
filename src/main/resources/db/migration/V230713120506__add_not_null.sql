/*

Разработка-193-Изменение таблицы admin.t_letter_template
https://gitlab.inlinegroup-c.ru/mirror/backend/service-app-admin/-/issues/23#note_78401

*/

UPDATE "admin".t_letter_template SET status = 'DRAFT' WHERE status IS NULL ;
ALTER TABLE "admin".t_letter_template ALTER COLUMN status SET NOT NULL;
ALTER TABLE "admin".t_letter_template ALTER COLUMN status SET DEFAULT 'DRAFT';
