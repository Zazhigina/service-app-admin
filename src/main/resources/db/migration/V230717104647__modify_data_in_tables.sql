/*

1.
Разработка-198-Загрузка данных для таблицы admi.t_variable
https://gitlab.inlinegroup-c.ru/mirror/model/-/issues/115#note_78825

2.
Разработка-193-Добавление шаблонов
https://gitlab.inlinegroup-c.ru/mirror/model/-/issues/117

*/

-- 1:

UPDATE "admin".t_variable tv SET default_val = NULL WHERE default_val = '' ;

UPDATE "admin".t_variable tv 
SET 
    name = '{Ссылка_поставщика_КП}',
    description  = 'Индивидуальная ссылка поставщика на КП'
WHERE 
    id = 1015002 
;

INSERT INTO "admin".t_variable (id, "name", description, default_val, create_date, create_user, last_update_date, last_update_user) VALUES(1026003, '{Значение_GUID}', 'Значение GUID', NULL, '2023-07-17 14:10:31.329', NULL, NULL, NULL);
INSERT INTO "admin".t_variable (id, "name", description, default_val, create_date, create_user, last_update_date, last_update_user) VALUES(1027003, '{Ссылка_поставщика_ЗЗ}', 'vvvvvИндивидуальная ссылка поставщика на ЗЗvvvvvvvvv', NULL, '2023-07-17 14:10:31.329', NULL, NULL, NULL);
INSERT INTO "admin".t_variable (id, "name", description, default_val, create_date, create_user, last_update_date, last_update_user) VALUES(1028003, '{Ссылка_инициатора_запрос_ЕР}', 'Ссылка для инициатора на запрос ЕР', NULL, '2023-07-17 14:10:31.329', NULL, NULL, NULL);
INSERT INTO "admin".t_variable (id, "name", description, default_val, create_date, create_user, last_update_date, last_update_user) VALUES(1029003, '{Ссылка_инициатора_запрос_МА}', 'Ссылка для инициатора на запрос МА', NULL, '2023-07-17 14:10:31.329', NULL, NULL, NULL);
INSERT INTO "admin".t_variable (id, "name", description, default_val, create_date, create_user, last_update_date, last_update_user) VALUES(1030003, '{Электронная_почта_технический_ящик}', 'Технический почтовый ящик для отправки писем запросов поставщикам', NULL, '2023-07-17 14:10:31.329', NULL, NULL, NULL);
INSERT INTO "admin".t_variable (id, "name", description, default_val, create_date, create_user, last_update_date, last_update_user) VALUES(1031003, '{Тема_письма_запроса_КП}', 'Тема письма для запроса КП', NULL, '2023-07-17 14:10:31.329', NULL, NULL, NULL);

-- 2:

INSERT INTO "admin".t_letter_template (id, letter_type, title, letter_sample, create_date, create_user, last_update_date, last_update_user, type_template, acceptable_document_format, status) VALUES(1036003, 'MA_INTEREST_OF_REQUEST_RESULT_CONTRACTOR', 'Effect. Анализ рынка - ответ на запрос заинтересованности', NULL, '2023-07-17 14:36:53.418', NULL, NULL, NULL, 'TEMPLATE', NULL, 'DRAFT');
INSERT INTO "admin".t_letter_template (id, letter_type, title, letter_sample, create_date, create_user, last_update_date, last_update_user, type_template, acceptable_document_format, status) VALUES(1037003, 'MA_INTEREST_OF_REQUEST_RESULT_INITIATOR', 'Effect. Анализ рынка - поступили ответы от Поставщика', NULL, '2023-07-17 14:36:53.418', NULL, NULL, NULL, 'TEMPLATE', NULL, 'DRAFT');
INSERT INTO "admin".t_letter_template (id, letter_type, title, letter_sample, create_date, create_user, last_update_date, last_update_user, type_template, acceptable_document_format, status) VALUES(1038003, 'TECHNICAL_TASK_TEMPLATE', 'Шаблон технического задания', NULL, '2023-07-17 14:36:53.418', NULL, NULL, NULL, 'DOCUMENT', NULL, 'DRAFT');
