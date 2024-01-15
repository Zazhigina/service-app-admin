/*
Разработка-007-Корректировка имен 2-х переменных для рассылки запросов поставщикам МТР
https://gitlab.inlinegroup-c.ru/mirror/backend/service-app-admin/-/issues/66
*/

UPDATE "admin".t_letter_template
	SET letter_type='MTR_OFFER_REQUEST_ONE_CUSTOMER',title='Effect. Анализ рынка - запрос коммерческого предложения по МТР (один Заказчик)'
	WHERE id=55005;
UPDATE "admin".t_letter_template
	SET letter_type='MTR_OFFER_REQUEST_MANY_CUSTOMERS',title='Effect. Анализ рынка - запрос коммерческого предложения по МТР (несколько Заказчиков)'
	WHERE id=56005;