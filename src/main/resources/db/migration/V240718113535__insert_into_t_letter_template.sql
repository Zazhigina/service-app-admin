/*
    Dev-54 | bp | Реализация доп настроек ведения данных для справки в сервисе администрирования
    https://gitlab.inlinegroup-c.ru/mirror/backend/service-app-admin/-/issues/121
*/

INSERT INTO t_letter_template (id,letter_type,title,letter_sample,create_date,type_template,acceptable_document_format,status) VALUES
	 (291005,'COMMON_USER_GUIDE','Инструкция пользователя по общим настройкам Effect',NULL,'2024-07-18 11:31:39.169236','DOCUMENT','pdf','DRAFT'),
	 (292005,'MTR_COMMON_USER_GUIDE','Инструкция пользователя МТР по общим настройкам Effect',NULL,'2024-07-18 11:31:39.211704','DOCUMENT','pdf','DRAFT');
