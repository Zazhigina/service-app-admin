/*
Разработка-948-Заполнение t_letter_template
https://gitlab.inlinegroup-c.ru/mirror/backend/service-app-admin/-/issues/100
*/

INSERT INTO "admin".t_letter_template (id, letter_type, title, letter_sample, create_date, create_user, last_update_date, last_update_user, type_template, acceptable_document_format, status) VALUES
(241005, 'EP_COMMERCIAL_OFFER_RECEIVED_NOTICE', 'Effect - Расчет цены закупки', NULL, '2024-04-26 14:50:00.001', NULL, NULL, NULL, 'TEMPLATE', NULL, 'DRAFT'),
(242005, 'EP_COMMERCIAL_OFFER_RECEIVED_UPDATED_NOTICE', 'Effect - Расчет цены закупки', NULL, '2024-04-26 14:50:00.001', NULL, NULL, NULL, 'TEMPLATE', NULL, 'DRAFT'),
(243005, 'EP_COMMERCIAL_OFFER_PROLONGATION_INITIATOR_NOTICE', 'Effect - Расчет цены закупки', NULL, '2024-04-26 14:50:00.001', NULL, NULL, NULL, 'TEMPLATE', NULL, 'DRAFT'),
(244005, 'EP_COMMERCIAL_OFFER_ACCEPTING_COMING_END_INITIATOR_NOTICE', 'Effect - Расчет цены закупки', NULL, '2024-04-26 14:50:00.001', NULL, NULL, NULL, 'TEMPLATE', NULL, 'DRAFT'),
(245005, 'EP_COMMERCIAL_OFFER_ACCEPTING_COMPLETED_INITIATOR_NOTICE', 'Effect - Расчет цены закупки', NULL, '2024-04-26 14:50:00.001', NULL, NULL, NULL, 'TEMPLATE', NULL, 'DRAFT'),
(246005, 'MA_INTEREST_OF_REQUEST_RESULT_INITIATOR_NOTICE', 'Effect - Поиск поставщиков', NULL, '2024-04-26 14:50:00.001', NULL, NULL, NULL, 'TEMPLATE', NULL, 'DRAFT'),
(247005, 'NEW_CONTRACTOR_CHAT_MESSAGE_NOTICE', 'Effect - Сообщение от поставщика', NULL, '2024-04-26 14:50:00.001', NULL, NULL, NULL, 'TEMPLATE', NULL, 'DRAFT');