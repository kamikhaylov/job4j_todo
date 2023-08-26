create table users (
    id          serial primary key,
    name        varchar not null,
    login       varchar not null unique,
    password    varchar not null,

    constraint login_unique unique (login)
);

comment on table users is 'Пользователи';
comment on column users.id is 'Идентификатор пользователя';
comment on column users.name is 'Имя';
comment on column users.login is 'Логин';
comment on column users.password is 'Пароль';