/*
Разработка-952-Ведение режимов работы системы effect. Расширение списка сервисов.
https://gitlab.inlinegroup-c.ru/mirror/backend/service-app-admin/-/issues/157
*/

INSERT INTO "admin".t_app_control ("name", description) VALUES('ui-prcat', 'Клиентская часть функционала, обеспечивающая автоматизированную поддержку функций Администратора Справочника для Справочника наименований расценок');
INSERT INTO "admin".t_app_control ("name", description) VALUES('ui-ebase', 'База опыта, сервис для ведения и просмотра информации об опыте поставщиков: данные о договорах (внутренних и внешних) и статуса участия во внутренних закупках.');
INSERT INTO "admin".t_app_control ("name", description) VALUES('ui-pdd', 'Клиентская часть функционала Конструктор документов (Техническое задание)');
INSERT INTO "admin".t_app_control ("name", description) VALUES('ui-analytics', 'Клиентская часть функционала, обеспечивающая автоматизированную поддержку функций Специалиста по формированию отчетности для Аналитики');
INSERT INTO "admin".t_app_control ("name", description) VALUES('ui-aso', 'Анализ заявок, сервис для выполнения анализа, оценки и сопоставления заявок на участие в закупочных процедурах');
INSERT INTO "admin".t_app_control ("name", description) VALUES('ui-mtr-ep', 'Клиентская часть функционала, обеспечивающая автоматизированную поддержку функций организатора закупок для расчета цены закупки МТР');
INSERT INTO "admin".t_app_control ("name", description) VALUES('ui-help', 'Клиентская часть функционала, обеспечивающая просмотр инструкций и вопросы\ответы для работы сервисов');
INSERT INTO "admin".t_app_control ("name", description) VALUES('ui-notification', 'Клиентская часть сервиса оповещений');
INSERT INTO "admin".t_app_control ("name", description) VALUES('ui-rpu', 'Клиентская часть функционала Реестра потенциальных участников закупок');
INSERT INTO "admin".t_app_control ("name", description) VALUES('ui-favourite', 'Клиентская часть функционала, обеспечивающая механизм управления избранными списками Инициатором');