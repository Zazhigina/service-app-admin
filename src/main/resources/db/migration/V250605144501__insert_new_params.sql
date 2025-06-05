/*
    Dev-1119-создание параметра для периода опроса непрочитанных сообщений \ шаблонов ОИ
    #196
*/

INSERT INTO "admin".a_param
    (key,name,val)
VALUES
    ('UNREAD_COUNT_REQUEST_PERIOD', 'Периодичность запроса непрочитанных сообщений (в секундах)', NULL);

INSERT INTO "admin".t_letter_template
    (id, letter_type, title, letter_sample, create_date, create_user, last_update_date, last_update_user, type_template, acceptable_document_format, status)
VALUES
    (543005, 'EP_USER_GUIDE_COST', 'Инструкция с описанием операций сервиса "Расчет цены закупки" Затратный метод', NULL,'2025-06-05 14:45:01.321', NULL, NULL, NULL, 'DOCUMENT', 'pdf', 'DRAFT'),
    (544005, 'EP_USER_GUIDE_EXECUTED_CONTRACT', 'Инструкция с описанием операций сервиса "Расчет цены закупки" Метод исполненных контрактов', NULL,'2025-06-05 14:45:01.321', NULL, NULL, NULL, 'DOCUMENT', 'pdf', 'DRAFT'),
    (545005, 'EP_USER_GUIDE_COMBO', 'Инструкция с описанием операций сервиса "Расчет цены закупки" Смешанный метод', NULL,'2025-06-05 14:45:01.321', NULL, NULL, NULL, 'DOCUMENT', 'pdf', 'DRAFT');