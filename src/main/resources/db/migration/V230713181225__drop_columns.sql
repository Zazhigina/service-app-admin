/*

Разработка-193-Изменение структры БД для хранения переменных шаблона
https://gitlab.inlinegroup-c.ru/mirror/backend/service-app-admin/-/issues/21#note_78506

*/

ALTER TABLE "admin".t_letter_template_variable DROP COLUMN "name";
ALTER TABLE "admin".t_letter_template_variable DROP COLUMN val;
