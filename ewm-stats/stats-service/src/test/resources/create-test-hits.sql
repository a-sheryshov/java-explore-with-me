insert into APPS(APP_NAME)
values ('ewm-main-service');

insert into hits (app_id, uri, ip, timestamp)
values (1, '/events/1', '192.168.0.1', '2023-07-01 12:00:00'),
       (1, '/events/1', '192.168.0.2', '2023-06-01 12:00:00'),
       (1, '/events/2', '192.168.0.2', '2023-07-01 12:00:00');