/*
    DEV-11-SRV | partnerlk | Согласие на обработку персональных данных
    https://gitlab.inlinegroup-c.ru/mirror/backend/service-app-admin/-/issues/163
*/

INSERT INTO admin.t_letter_template
(letter_type, title, letter_sample, create_date, create_user, last_update_date, last_update_user, type_template, acceptable_document_format, status)
VALUES
('PARTNERLK_PERSONAL_DATA_TEXT', 'Текст "Согласие на обработку ПДн" в окне', NULL, '2024-11-07 18:42:22.588', NULL, NULL, NULL, 'TEMPLATE', 'html', 'DRAFT'),
('PARTNERLK_PERSONAL_DATA_DOC', 'Согласие на обработку ПДн', NULL, '2024-11-07 18:42:22.598', NULL, NULL, NULL, 'TEMPLATE', 'html', 'DRAFT'),
('PARTNERLK_PERSONAL_DATA_POLICY', 'Политика обработки персональных данных', NULL, '2024-11-07 18:42:22.604', NULL, NULL, NULL, 'TEMPLATE', 'html', 'DRAFT');
