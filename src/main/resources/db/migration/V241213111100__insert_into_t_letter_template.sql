/*
    Разработка-37-Учет новых сервисов в функционале справки
    https://gitlab.inlinegroup-c.ru/mirror/backend/service-app-admin/-/issues/181
*/

INSERT INTO t_letter_template (id,letter_type,title,letter_sample,create_date,create_user,last_update_date,last_update_user,type_template,acceptable_document_format,status) VALUES
	 (511005,'PDD_USER_GUIDE_EVALM_INITIATOR','Инструкция по созданию документов (Инициатор). Методика оценки',NULL,'2024-12-13 11:14:26.74285',NULL,NULL,NULL,'DOCUMENT','pdf','DRAFT'),
	 (510005,'PDD_USER_GUIDE_EVALM_MODERATOR','Инструкция по созданию шаблонов документов (Модератор). Методика оценки',NULL,'2024-12-13 11:14:26.738652',NULL,NULL,NULL,'DOCUMENT','pdf','DRAFT'),
	 (509005,'PDD_USER_GUIDE_TSPEC_INITIATOR','Инструкция по созданию документов (Инициатор). ТЗ',NULL,'2024-12-13 11:14:26.734806',NULL,NULL,NULL,'DOCUMENT','pdf','DRAFT'),
	 (508005,'PDD_USER_GUIDE_TSPEC_MODERATOR','Инструкция по созданию шаблонов документов (Модератор). ТЗ',NULL,'2024-12-13 11:14:26.73093',NULL,NULL,NULL,'DOCUMENT','pdf','DRAFT'),
	 (507005,'ANALYTICS_USER_GUIDE','Инструкция по формированию аналитики',NULL,'2024-12-13 11:14:26.723023',NULL,NULL,NULL,'DOCUMENT','pdf','DRAFT');