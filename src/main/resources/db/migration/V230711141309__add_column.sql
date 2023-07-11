/*

Разработка-193-Изменение таблицы admin.t_letter_template
https://gitlab.inlinegroup-c.ru/mirror/backend/service-app-admin/-/issues/23

*/

ALTER TABLE "admin".t_letter_template ADD status varchar NULL;
COMMENT ON COLUMN "admin".t_letter_template.status IS 'Статус';

ALTER TABLE "admin".t_letter_template ALTER COLUMN title DROP NOT NULL;
ALTER TABLE "admin".t_letter_template ALTER COLUMN letter_sample DROP NOT NULL;
