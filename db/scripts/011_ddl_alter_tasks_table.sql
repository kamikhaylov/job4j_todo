alter table tasks drop column created;
alter table tasks add column created timestamp without time zone default now();

comment on column tasks.created is 'Дата создания';