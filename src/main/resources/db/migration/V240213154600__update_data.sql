/*
Разработка-856-выгрузка шаблона ТЗ для транспортных услуг. БД. Переименование шаблона
https://gitlab.inlinegroup-c.ru/mirror/backend/service-app-admin/-/issues/75
*/

UPDATE "admin".t_letter_template
SET letter_type = 'EP_TECHNICAL_TASK_TEMPLATE_AUTO_TRANSPORT', title = 'Шаблон технического задания "Транспортные услуги"'
WHERE id = 17005;
