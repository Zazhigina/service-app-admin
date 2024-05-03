/*
    Разработка-38-Реализация ведения данных для справки в сервисе администрирования
    https://gitlab.inlinegroup-c.ru/mirror/backend/service-app-admin/-/issues/77
*/

INSERT INTO t_letter_template (id,letter_type,title,letter_sample,create_date,type_template,acceptable_document_format,status) VALUES
	 (251005,'MA_USER_GUIDE','Инструкция с описанием операций сервиса "Поиск поставщиков"',NULL,'2024-05-03 14:00:48.932611','DOCUMENT','pdf','DRAFT'),
	 (252005,'EP_USER_GUIDE','Инструкция с описанием операций сервиса "Расчет цены закупки"',NULL,'2024-05-03 14:00:48.944967','DOCUMENT','pdf','DRAFT');
