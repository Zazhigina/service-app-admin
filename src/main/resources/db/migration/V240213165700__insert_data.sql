/*
Разработка-856-выгрузка шаблона ТЗ для транспортных услуг. БД. Переименование шаблона и добавление форматов .xls, .xlsx
https://gitlab.inlinegroup-c.ru/mirror/backend/service-app-admin/-/issues/75
*/

INSERT INTO "admin".t_letter_template_acceptable_document_format_enum (id, "name", description) VALUES(5, 'xls', NULL);
INSERT INTO "admin".t_letter_template_acceptable_document_format_enum (id, "name", description) VALUES(6, 'xlsx', NULL);