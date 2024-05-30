/*
    Разработка-54-Реализация доп настроек ведения данных для справки в сервисе администрирования
    https://gitlab.inlinegroup-c.ru/mirror/backend/service-app-admin/-/issues/113
*/

INSERT INTO t_letter_template (id,letter_type,title,letter_sample,create_date,type_template,acceptable_document_format,status) VALUES
	 (271005,'MTR_EP_USER_GUIDE_1','Инструкция "ОИ Закупщик МТР"',NULL,'2024-05-30 15:07:58.798855','DOCUMENT','pdf','DRAFT'),
	 (272005,'MTR_EP_USER_GUIDE_2','Инструкция "ОИ Специалист по поддержке БП МТР"',NULL,'2024-05-30 15:07:58.810542','DOCUMENT','pdf','DRAFT'),
	 (273005,'MTR_EP_USER_GUIDE_3','Инструкция "ОИ Специалист по закупкам МТР (базовая роль)"',NULL,'2024-05-30 15:07:58.815516','DOCUMENT','pdf','DRAFT'),
	 (274005,'MTR_EP_PARTNER_GUIDE','Инструкция "ОИ Поставщик МТР заполнение КП"',NULL,'2024-05-30 15:07:58.819754','DOCUMENT','pdf','DRAFT'),
	 (275005,'MTR_MA_USER_GUIDE','Инструкция "ОИ Поиск поставщиков Закупщик МТР"',NULL,'2024-05-30 15:07:58.825644','DOCUMENT','pdf','DRAFT'),
	 (276005,'MTR_MA_PARTNER_GUIDE','Инструкция "ОИ Поставщик МТР ответ на запрос заинтересованности"',NULL,'2024-05-30 15:07:58.830018','DOCUMENT','pdf','DRAFT'),
	 (277005,'PRCAT_USER_GUIDE_EDIT','Инструкция "Ведение Справочника наименований расценок"',NULL,'2024-05-30 15:07:58.8347','DOCUMENT','pdf','DRAFT'),
	 (278005,'PRCAT_USER_GUIDE_VIEW','Инструкция "Просмотр Справочника наименований расценок"',NULL,'2024-05-30 15:07:58.839397','DOCUMENT','pdf','DRAFT');