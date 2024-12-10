/*
Разработка-1004-Включение параметров NSI_MANUAL_MAINTENANCE* в спринт для переноса по ландшафтам
https://gitlab.inlinegroup-c.ru/mirror/backend/service-app-admin/-/issues/178
*/


INSERT INTO "admin".a_param ("key","name",val)
	VALUES ('NSI_MANUAL_MAINTENANCE_INITIATOR','NSI Иницаторы','API');
INSERT INTO "admin".a_param ("key","name",val)
	VALUES ('NSI_MANUAL_MAINTENANCE_SEGMENT','NSI Сегменты','API');

INSERT INTO "admin".a_param ("key","name",val)
	VALUES ('NSI_MANUAL_MAINTENANCE_REGION','NSI Регионы','API');
INSERT INTO "admin".a_param ("key","name",val)
	VALUES ('NSI_MANUAL_MAINTENANCE_UNIT','NSI ЕИ','API');
INSERT INTO "admin".a_param ("key","name",val)
	VALUES ('NSI_MANUAL_MAINTENANCE_SUPPLIER','NSI Единый справочник контрагентов (ЕСК)','API');
INSERT INTO "admin".a_param ("key","name",val)
	VALUES ('NSI_MANUAL_MAINTENANCE_SERVICE','NSI Услуги','API');
INSERT INTO "admin".a_param ("key","name",val)
	VALUES ('NSI_MANUAL_MAINTENANCE_MTR','NSI Классы МТР','API');
INSERT INTO "admin".a_param ("key","name",val)
	VALUES ('NSI_MANUAL_MAINTENANCE_SSEGMENT','NSI Сегметы/подсегменты для услуг','API');
INSERT INTO "admin".a_param ("key","name",val)
	VALUES ('NSI_MANUAL_MAINTENANCE_OKVED','NSI ОКВЭД2','API');
INSERT INTO "admin".a_param ("key","name",val)
	VALUES ('NSI_MANUAL_MAINTENANCE_OKATO','NSI ОКАТО','API');
INSERT INTO "admin".a_param ("key","name",val)
	VALUES ('NSI_MANUAL_MAINTENANCE_CURRENCY','NSI Валюты','API');
INSERT INTO "admin".a_param ("key","name",val)
	VALUES ('NSI_MANUAL_MAINTENANCE_OKPD','NSI ОКПД2','API');
INSERT INTO "admin".a_param ("key","name",val)
	VALUES ('NSI_MANUAL_MAINTENANCE_CUSTOMER','NSI Заказчики','API');
INSERT INTO "admin".a_param ("key","name",val)
	VALUES ('NSI_MANUAL_MAINTENANCE_ORGANIZATOR','NSI Организаторы','API');

update "admin".a_param
set "name" = 'Период приближающейся даты ответа на запрос'
where key = 'REQUEST_ANSWER_SOON_PERIOD';