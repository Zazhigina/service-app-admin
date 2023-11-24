/*

Разработка-741-шаблоны для размещения ОИ Поставщика по работе с Effect
https://gitlab.inlinegroup-c.ru/mirror/backend/service-app-admin/-/issues/46
*/

--insert into t_letter_template 

INSERT INTO "admin".t_letter_template (id,letter_type,title,letter_sample,create_date,create_user,last_update_date,last_update_user,type_template,acceptable_document_format,status) VALUES
	 (51005,'MA_PARTNER_GUIDE','Инструкция Поставщика "Запрос заинтересованности"',NULL,'2023-11-24 16:24:27.922013',NULL,NULL,NULL,'DOCUMENT',NULL,'DRAFT'),
	 (52005,'EP_PARTNER_GUIDE_COMMON','Инструкция Поставщика "КП: Общий профиль"',NULL,'2023-11-24 16:24:27.931579',NULL,NULL,NULL,'DOCUMENT',NULL,'DRAFT'),
	 (53005,'EP_PARTNER_GUIDE_AUTO_TRANSPORT','Инструкция Поставщика "КП: Транспортные услуги"',NULL,'2023-11-24 16:24:27.936498',NULL,NULL,NULL,'DOCUMENT',NULL,'DRAFT'),
	 (54005,'EP_PARTNER_GUIDE_IT_LICENSE','Инструкция Поставщика "КП: ИТ-лицензии"',NULL,'2023-11-24 16:24:27.940435',NULL,NULL,NULL,'DOCUMENT',NULL,'DRAFT'),
	 (55005,'MTR_OFFER_REQUEST','Effect. Анализ рынка - запрос коммерческого предложения по МТР',NULL,'2023-11-24 16:24:27.944446',NULL,NULL,NULL,'TEMPLATE',NULL,'DRAFT'),
	 (56005,'MTR_OFFER_REQUEST_TP','Effect. Анализ рынка - запрос коммерческого и технического предложений по МТР',NULL,'2023-11-24 16:24:27.948501',NULL,NULL,NULL,'TEMPLATE',NULL,'DRAFT'),
	 (57005,'MTR_OFFER_REQUEST_CANCEL','Effect. Анализ рынка - отмена запроса коммерческих предложений по МТР {Предмет закупки}',NULL,'2023-11-24 16:24:27.957375',NULL,NULL,NULL,'TEMPLATE',NULL,'DRAFT');


--insert into t_letter_template_acceptable_document_format_enum

INSERT INTO "admin".t_letter_template_acceptable_document_format_enum (id, "name", description) VALUES(4, 'pdf', NULL);
