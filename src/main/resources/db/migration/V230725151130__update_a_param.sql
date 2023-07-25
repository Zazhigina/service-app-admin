/*

Разработка-199-Сценарий 4. Заполнение поставщиком КП Общий профиль (БД)
https://gitlab.inlinegroup-c.ru/mirror/model/-/issues/92#note_80647

*/

UPDATE "admin".a_param SET name = 'Технический почтовый ящик для приема быстрых ответов от поставщиков' WHERE "key" = 'PARTNER_EXCHANGE.EMAIL_SENDER';
UPDATE "admin".a_param SET name = 'Технический почтовый ящик для отправки писем запросов поставщикам' WHERE "key" = 'PARTNER_EXCHANGE.EMAIL_NOREPLY';
