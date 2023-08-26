create table tasks (
    id              serial primary key,
    description     varchar,
    created         timestamp,
    done            boolean
);

comment on table tasks is 'Задания';
comment on column tasks.id is 'Идентификатор задания';
comment on column tasks.description is 'Описание';
comment on column tasks.created is 'Дата создания';
comment on column tasks.done is 'Выполнено';