/*
Разработка-969-Создание шаблона письма для запроса снижения стоимости
https://gitlab.inlinegroup-c.ru/mirror/backend/service-app-admin/-/issues/104
*/

-- Добавление шаблонов запросов на снижение стоимости КП
INSERT INTO admin.t_letter_template
(letter_type, title, letter_sample, create_date, create_user, last_update_date, last_update_user, type_template, acceptable_document_format, status)
VALUES
    ('EP_COMMERCIAL_OFFER_REDUCTION_COST', 'Effect. Анализ рынка  – запрос на снижение стоимости коммерческого предложения', NULL, '2024-06-26 09:57:58.839', NULL, NULL, NULL, 'TEMPLATE', 'html', 'DRAFT'),
    ('EP_COMMERCIAL_OFFER_REDUCTION_COST_TP', 'Effect. Анализ рынка  – запрос на снижение стоимости коммерческого предложения с техническим предложением', NULL, '2024-06-26 09:57:59.839', NULL, NULL, NULL, 'TEMPLATE', 'html', 'DRAFT');