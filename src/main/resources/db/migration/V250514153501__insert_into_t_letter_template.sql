/*
Dev-1160-создание нового параметра для загрузки шаблона
#189
*/

INSERT INTO "admin".t_letter_template
(id, letter_type, title, letter_sample, create_date, create_user, last_update_date, last_update_user, type_template, acceptable_document_format, status)
VALUES (520005, 'COMMON_DOWNLOAD_TEMPLETE', 'Шаблон загрузки для шаблона КП Общего профиля', NULL,'2025-05-14 15:35:01.321', NULL, NULL, NULL, 'DOCUMENT', 'xlsx', 'DRAFT');
