INSERT INTO sys_oauth_client (id, client_id, application_name, client_secret, authorized_grant_types, authorities,
                                 scope, web_server_redirect_uri, access_token_validity, refresh_token_validity, remarks)
VALUES (1, 'SampleClientId', 'SampleClientId 测试应用', '$2a$10$gcrWom7ubcRaVD1.6ZIrIeJP0mtPLH5J9V/.8Qth59lZ4B/5HMq96',
        'authorization_code,refresh_token,password', 'ROLE_TRUSTED_CLIENT', 'user_info',
        'http://www.baidu.com', 3600, 2592000, '测试明文:tgb.258');

INSERT INTO sys_scope (id, scope, definition)
values (1, 'user_info', '昵称、头像、性别信息');

insert into sys_role(id, role_name)
values (1, 'ROLE_SUPER'),
       (2, 'ROLE_ADMIN'),
       (3, 'ROLE_USER');

INSERT INTO sys_account (id, username, password, account_open_code, nick_name, remarks)
VALUES (1, 'admin', '$2a$10$gcrWom7ubcRaVD1.6ZIrIeJP0mtPLH5J9V/.8Qth59lZ4B/5HMq96', '1', '张三', '测试明文:tgb.258'),
       (3, 'lany', '$2a$10$gcrWom7ubcRaVD1.6ZIrIeJP0mtPLH5J9V/.8Qth59lZ4B/5HMq96', '3', '李四', '测试明文:tgb.258');

insert into sys_account_roles(user_id, role_id)
values (1, 1),(3, 3);
