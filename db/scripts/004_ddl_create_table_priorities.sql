create table priorities
(
    id          serial primary key,
    name        varchar not null,
    position    int
);

comment on table priorities is 'Приоритеты';
comment on column priorities.id is 'Идентификатор задания';
comment on column priorities.name is 'Наименнование';
comment on column priorities.position is 'Позиция';
