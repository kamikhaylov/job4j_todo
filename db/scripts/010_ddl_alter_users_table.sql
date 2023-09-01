alter table users add column user_zone varchar;

comment on column users.user_zone is 'Часовой пояс пользователя';