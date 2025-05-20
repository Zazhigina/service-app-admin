/*
    Dev-1162-Создание параметров для шаблонов писем для работы с публикацией запросов РЦЗ + ПП
    #191
    Dev-1143-Добавление параметра для таблицы маппинга
    #194
*/

INSERT INTO "admin".t_letter_template
    (id, letter_type, title, letter_sample, create_date, create_user, last_update_date, last_update_user, type_template, acceptable_document_format, status)
VALUES
    (521005, 'EP_COMMERCIAL_OFFER_PUBLISHING', 'Effect. Открытый анализ рынка с запросом коммерческого предложения - публикация запроса на сайте Закупки Газпром нефть)', NULL,'2025-05-20 16:45:01.321', NULL, NULL, NULL, 'DOCUMENT', 'html', 'DRAFT'),
    (522005, 'EP_COMMERCIAL_OFFER_PUBLISHING_UPDATE_INITIATOR', 'Effect. Открытый анализ рынка с запросом коммерческого предложения - изменение публикации запроса на сайте Закупки Газпром нефть', NULL,'2025-05-20 16:45:01.321', NULL, NULL, NULL, 'DOCUMENT', 'html', 'DRAFT'),
    (523005, 'EP_COMMERCIAL_OFFER_PUBLISHING_CANCEL_INITIATOR', 'Effect. Открытый анализ рынка с запросом коммерческого предложения - снятие публикации запроса на сайте Закупки Газпром нефть', NULL,'2025-05-20 16:45:01.321', NULL, NULL, NULL, 'DOCUMENT', 'html', 'DRAFT'),
    (524005, 'MA_INTEREST_OF_REQUEST_PUBLISHING', 'Effect. Открытый анализ рынка - публикация запроса заинтересованности на сайте Закупки Газпром нефть', NULL,'2025-05-20 16:45:01.321', NULL, NULL, NULL, 'DOCUMENT', 'html', 'DRAFT'),
    (525005, 'MA_INTEREST_OF_REQUEST_PUBLISHING_UPDATE_INITIATOR', 'Effect. Открытый анализ рынка - изменение публикации запроса заинтересованности на сайте Закупки Газпром нефть', NULL,'2025-05-20 16:45:01.321', NULL, NULL, NULL, 'DOCUMENT', 'html', 'DRAFT'),
    (526005, 'MA_INTEREST_OF_REQUEST_UPDATE_CONTRACTOR', 'Effect. Анализ рынка - изменение запроса заинтересованности', NULL,'2025-05-20 16:45:01.321', NULL, NULL, NULL, 'DOCUMENT', 'html', 'DRAFT'),
    (527005, 'MA_INTEREST_OF_REQUEST_PUBLISHING_CANCEL_INITIATOR', 'Effect. Открытый анализ рынка - снятие публикации запроса заинтересованности на сайте Закупки Газпром нефть', NULL,'2025-05-20 16:45:01.321', NULL, NULL, NULL, 'DOCUMENT', 'html', 'DRAFT'),
    (528005, 'MANUAL_PUBLISH_NOTE', 'Блок ручного описания для публикации', NULL,'2025-05-20 16:45:01.321', NULL, NULL, NULL, 'DOCUMENT', 'html', 'DRAFT'),
    (529005, 'PUBLISH_NOTE_FOR_REFERENCE', 'Блок описания для публикации со ссылкой', NULL,'2025-05-20 16:45:01.321', NULL, NULL, NULL, 'DOCUMENT', 'html', 'DRAFT'),
    (530005, 'PUBLISH_NOTE_FOR_CONTACTS', 'Блок описания для публикации с контактными данными', NULL,'2025-05-20 16:45:01.321', NULL, NULL, NULL, 'DOCUMENT', 'html', 'DRAFT');

INSERT INTO "admin".t_variable
    (id, name, description, default_val, create_date, create_user, last_update_date, last_update_user)
VALUES
    (531005, '{Ссылка_сайта_Закупки}', 'Ссылка сайта Закупки Газпром нефть', NULL, '2025-05-20 16:45:01.321', NULL, NULL, NULL),
    (532005, '{Дата_время_отправки_запроса_на_сайт_Закупки}', 'Дата и время отправки запроса на сайт Закупки', NULL, '2025-05-20 16:45:01.321', NULL, NULL, NULL),
    (533005, '{Ручное_описание_публикации}', 'Блок описания для публикации, введенный вручную', NULL, '2025-05-20 16:45:01.321', NULL, NULL, NULL),
    (534005, '{Описание_ссылки_публикации}', 'Блок описания для публикации со ссылкой Effect', NULL, '2025-05-20 16:45:01.321', NULL, NULL, NULL),
    (535005, '{Контактные_данные_Инициатора}', 'Блок описания для публикации с контактными данными инициатора', NULL, '2025-05-20 16:45:01.321', NULL, NULL, NULL),
    (536005, '{Ссылка_поставщика_сайт_Закупки_КП}', 'Ссылка для поставщика для перехода с сайта закупки в КП', NULL, '2025-05-20 16:45:01.321', NULL, NULL, NULL),
    (537005, '{Ссылка_поставщика_сайт_Закупки_ЗЗ}', 'Ссылка для поставщика для перехода с сайта Закупки в ЗЗ', NULL, '2025-05-20 16:45:01.321', NULL, NULL, NULL),
    (538005, '{Категория_закупки}', 'Категория закупки', NULL, '2025-05-20 16:45:01.321', NULL, NULL, NULL),
    (539005, '{Место_поставки}', 'Место поставки товаров, оказания работ/услуг', NULL, '2025-05-20 16:45:01.321', NULL, NULL, NULL),
    (540005, '{Изменение_публикации}', 'Описание изменения опубликованного запроса на сайте', NULL, '2025-05-20 16:45:01.321', NULL, NULL, NULL),
    (541005, '{Измененный_параметр_ЗЗ}', 'Измененный параметр запроса заинтересованности', NULL, '2025-05-20 16:45:01.321', NULL, NULL, NULL),
    (542005, '{Неперсонифицированный_GUID}', 'GUID запроса для перехода с сайта Закупки', NULL, '2025-05-20 16:45:01.321', NULL, NULL, NULL);

INSERT INTO "admin".a_param
    (key,name,val)
VALUES
    ('URL_FOR_PUBLISH', 'URL для перехода к сайту Закупки Газпром нефть блок Планирование', NULL),
    ('NSI_MANUAL_MAINTENANCE_SOKPD', 'NSI ОКПД для услуг', 'MANUAL');