create table priorities
(
    id serial primary key,
    name text not null,
    position int
);

insert into priorities (name, position) values ('Низкий', 1);
insert into priorities (name, position) values ('Средний', 2);
insert into priorities (name, position) values ('Срочный', 3);

alter table tasks add column priority_id int references priorities(id);

update tasks set priority_id = (select id from priorities where name = 'Средний');