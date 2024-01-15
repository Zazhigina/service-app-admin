/*
c:\Users\chernishevae\Documents\JAVA\!_DB Migration Mirror\Admin\V240115120000__update_data.sql
https://gitlab.inlinegroup-c.ru/mirror/backend/service-app-admin/-/issues/69
*/

INSERT INTO "admin".t_letter_template (id, letter_type, title, letter_sample, create_date, create_user, last_update_date, last_update_user, type_template, acceptable_document_format, status) VALUES
(91005, 'EP_PARTNER_GUIDE_COST', 'Инструкция Поставщика "КП: Затратный метод"', NULL, '2024-01-15 19:00:36.510', NULL, NULL, NULL, 'DOCUMENT', NULL, 'DRAFT');
