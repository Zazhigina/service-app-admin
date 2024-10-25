/*
    Разработка-1048-Новые параметры_Хвостовики
    https://gitlab.inlinegroup-c.ru/mirror/backend/service-app-admin/-/issues/160
*/

INSERT INTO "admin".t_letter_template
(letter_type, title, type_template, acceptable_document_format, status)
VALUES('EP_PARTNER_GUIDE_SHANK', 'Инструкция Поставщика "КП: Хвостовики"', 'DOCUMENT', 'pdf', 'DRAFT');


INSERT INTO "admin".a_param
("key", "name", val)
VALUES('EP_SHANK_LOWEST_PURCHASE_OFFER_LIMIT', 'Минимальное количество КП для расчета цены по наименьшей стоимости', '3');