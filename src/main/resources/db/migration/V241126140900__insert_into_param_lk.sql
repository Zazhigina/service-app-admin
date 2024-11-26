/*
Разработка списка запросов заинтересованности в ЛК поставщика: параметры
https://gitlab.inlinegroup-c.ru/mirror/backend/service-app-admin/-/issues/173
*/


INSERT INTO "admin".a_param ("key","name",val)
	VALUES ('REQUEST_ARCHIVE_PERIOD','Период перевода запроса в архив','3');
INSERT INTO "admin".a_param ("key","name",val)
	VALUES ('REQUEST_ANSWER_SOON_PERIOD','Период прилижающейся даты ответа на запрос','3');