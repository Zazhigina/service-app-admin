/*
Разработка-12-Скрипт заполнения вопросов и ответов запроса заинтересованности МТР
https://gitlab.inlinegroup-c.ru/mirror/backend/service-app-admin/-/issues/90
*/

--изменение ограничения для таблицы вопросов
ALTER TABLE "admin".t_question DROP CONSTRAINT t_question_un;
ALTER TABLE "admin".t_question ADD CONSTRAINT t_question_un UNIQUE (code, owner);


--вставка вопросов заинтересованности для МТР
INSERT INTO "admin".t_question (id, name, order_no, actual_to, create_date, create_user, last_update_date, last_update_user, code, owner) VALUES
(11003, 'Заинтересованы ли Вы в участии в закупке МТР в соответствии с предметом планируемой закупки и Техническим заданием?', 1, NULL, '2024-03-27 16:28:05.291', NULL, NULL, NULL, 'PARTICIPATION_INTEREST', 'INTEREST_REQUEST_MTR'),
(12003, 'Имеется ли у Вас опыт поставки МТР в соответствии с предметом планируемой закупки и Техническим заданием?', 2, NULL, '2024-03-27 16:28:05.291', NULL, NULL, NULL, 'WORK_EXPERIENCE', 'INTEREST_REQUEST_MTR'),
(13003, 'Готовы ли Вы предоставить Коммерческое предложение?', 3, NULL, '2024-03-27 16:28:05.291', NULL, NULL, NULL, 'SEND_OFFER', 'INTEREST_REQUEST_MTR');

--изменение типа владельца
UPDATE "admin".t_question SET owner = 'INTEREST_REQUEST_SERVICE' WHERE owner = 'SERVICE_PRODUCT';

-- вставка соответствующих ответов
INSERT INTO "admin".t_answer_version (id, question_id, name, order_no, create_date, create_user, last_update_date, last_update_user, is_default, type) values
(11113, 11003, 'Да, подтверждаем ознакомление и заинтересованность в участии закупке МТР в соответствии с предметом планируемой закупки и Техническим заданием', 1, '2024-03-27 16:28:05.291', NULL, NULL, NULL, true, 'POSITIVE'),
(12113, 11003, 'Нет, не заинтересованы', 2, '2024-03-27 16:28:05.291', NULL, NULL, NULL, false, 'NEGATIVE'),
(11123, 12003, 'Да, имеется опыт поставки МТР', 1, '2024-03-27 16:28:05.291', NULL, NULL, NULL, true, 'POSITIVE'), 
(12123, 12003, 'Нет релевантного опыта работы', 2, '2024-03-27 16:28:05.291', NULL, NULL, NULL, false, 'NEGATIVE'),
(11133, 13003, 'Да, готовы предоставить Коммерческое предложение', 1, '2024-03-27 16:28:05.291', NULL, NULL, NULL, true, 'POSITIVE'),
(12133, 13003, 'Нет, не готовы', 2, '2024-03-27 16:28:05.291', NULL, NULL, NULL, false, 'NEGATIVE');
