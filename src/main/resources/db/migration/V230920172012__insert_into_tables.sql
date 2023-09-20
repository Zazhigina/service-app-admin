/*
Разработка-561-Перенос шаблонов писем в UAT
https://gitlab.inlinegroup-c.ru/mirror/backend/service-app-admin/-/issues/36

*/

--1 "admin".t_letter_template
DELETE FROM admin.t_letter_template;

DELETE FROM admin.t_letter_template_variable;

INSERT INTO "admin".t_letter_template (id,letter_type,title,letter_sample,create_date,create_user,last_update_date,last_update_user,type_template,acceptable_document_format,status) VALUES
	 (17005,'TECHNICAL_TASK_TEMPLATE','Шаблон технического задания',NULL,'2023-09-20 17:44:17.823',NULL,NULL,NULL,'DOCUMENT',NULL,'DRAFT'),

	 (18005,'MA_INTEREST_OF_REQUEST_RESULT_CONTRACTOR','Effect. Анализ рынка - ответ на запрос заинтересованности',NULL,'2023-09-20 17:44:17.823',NULL,NULL,NULL,'TEMPLATE',NULL,'DRAFT'),

	 (19005,'MA_INTEREST_OF_REQUEST_RESULT_INITIATOR','Effect. Анализ рынка - поступили ответы от Поставщика',NULL,'2023-09-20 17:44:17.823',NULL,NULL,NULL,'TEMPLATE',NULL,'DRAFT'),

	 (20005,'MA_INTEREST_OF_REQUEST','Effect. Анализ рынка - запрос заинтересованности',NULL,'2023-09-20 17:44:17.823',NULL,NULL,NULL,'TEMPLATE',NULL,'DRAFT'),

	 (21005,'TECHNICAL_PROPOSAL_TEMPLATE','Шаблон технического предложения',NULL,'2023-09-20 17:44:17.823',NULL,NULL,NULL,'DOCUMENT',NULL,'DRAFT'),

	 (22005,'EP_CHANGE_ACCEPTING_DEADLINE_REQUEST_TS_TP','Effect. Анализ рынка - изменение срока приема коммерческого и технического предложений',NULL,'2023-09-20 17:44:17.823',NULL,NULL,NULL,'TEMPLATE',NULL,'DRAFT'),

	 (23005,'EP_COMMERCIAL_OFFER_RECEIVED','Effect. Анализ рынка - поступило {Обновленное_коммерческое_предложение} предложение от Участника',NULL,'2023-09-20 17:44:17.823',NULL,NULL,NULL,'TEMPLATE',NULL,'DRAFT'),

	 (24005,'EP_COMMERCIAL_OFFER_REQUEST_NON_ACTUAL','Effect. Анализ рынка - неактуальность запроса коммерческого предложения',NULL,'2023-09-20 17:44:17.823',NULL,NULL,NULL,'TEMPLATE',NULL,'DRAFT'),

	 (25005,'EP_COMMERCIAL_OFFER_SENT','Effect. Анализ рынка - коммерческое предложение отправлено',NULL,'2023-09-20 17:44:17.823',NULL,NULL,NULL,'TEMPLATE',NULL,'DRAFT');

INSERT INTO "admin".t_letter_template (id,letter_type,title,letter_sample,create_date,create_user,last_update_date,last_update_user,type_template,acceptable_document_format,status) VALUES

	 (26005,'EP_COMMERCIAL_OFFER_ACCEPTING_COMPLETED_CONTRACTOR','Effect. Анализ рынка - завершен срок подачи коммерческого предложения',NULL,'2023-09-20 17:44:17.823',NULL,NULL,NULL,'TEMPLATE',NULL,'DRAFT'),

	 (27005,'EP_COMMERCIAL_OFFER_ACCEPTING_COMING_END_CONTRACTOR','Effect. Анализ рынка - завершается срок подачи коммерческого предложения',NULL,'2023-09-20 17:44:17.823',NULL,NULL,NULL,'TEMPLATE',NULL,'DRAFT'),

	 (28005,'EP_COMMERCIAL_OFFER_REQUEST_TS','Effect. Анализ рынка - {Первичный_повторный_запрос} коммерческого предложения',NULL,'2023-09-20 17:44:17.823',NULL,NULL,NULL,'TEMPLATE',NULL,'DRAFT'),

	 (29005,'EP_COMMERCIAL_OFFER_REQUEST_TS_TP','Effect. Анализ рынка - {Первичный_повторный_запрос} коммерческого и технического предложений',NULL,'2023-09-20 17:44:17.823',NULL,NULL,NULL,'TEMPLATE',NULL,'DRAFT'),

	 (30005,'EP_CHANGE_ACCEPTING_DEADLINE_REQUEST_TS','Effect. Анализ рынка - изменение срока приема коммерческого предложения',NULL,'2023-09-20 17:44:17.823',NULL,NULL,NULL,'TEMPLATE',NULL,'DRAFT'),

	 (31005,'EP_COMMERCIAL_OFFER_ACCEPTING_COMPLETED_INITIATOR','Effect. Анализ рынка - завершен срок приема КП',NULL,'2023-09-20 17:44:17.823',NULL,NULL,NULL,'TEMPLATE',NULL,'DRAFT'),

	 (32005,'EP_COMMERCIAL_OFFER_ACCEPTING_COMING_END_INITIATOR','Effect. Анализ рынка - завершается срок приема КП',NULL,'2023-09-20 17:44:17.823',NULL,NULL,NULL,'TEMPLATE',NULL,'DRAFT');

	 -- admin.t_variable

UPDATE admin.t_variable
SET default_val = null

