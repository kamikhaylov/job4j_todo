create table categories
(
    id      serial primary key,
    name    varchar not null
);

comment on table categories is 'Категорий';
comment on column categories.id is 'Идентификатор задания';
comment on column categories.name is 'Наименнование';

create table task_categories
(
    id              serial primary key,
    task_id         int not null references tasks (id),
    category_id     int not null references categories (id)
);

comment on table categories is 'Связи категорий и заданий';
comment on column categories.id is 'Идентификатор задания';
comment on column categories.name is 'Идентификатор категории';