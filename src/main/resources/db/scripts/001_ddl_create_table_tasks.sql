CREATE TABLE tasks (
    id SERIAL PRIMARY KEY,
    description TEXT,
    created TIMESTAMP,
    done BOOLEAN
);

COMMENT ON TABLE tasks IS 'Задания';
COMMENT ON COLUMN tasks.id IS 'Идентификатор задания';
COMMENT ON COLUMN tasks.description IS 'Описание';
COMMENT ON COLUMN tasks.created IS 'Дата создания';
COMMENT ON COLUMN tasks.done IS 'Выполнено';