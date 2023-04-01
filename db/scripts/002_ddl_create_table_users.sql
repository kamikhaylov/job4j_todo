CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    name varchar NOT NULL,
    login varchar NOT NULL UNIQUE,
    password varchar NOT NULL
);

ALTER TABLE users ADD CONSTRAINT login_unique UNIQUE (login);

COMMENT ON TABLE users IS 'Пользователи';
COMMENT ON COLUMN users.id IS 'Идентификатор пользователя';
COMMENT ON COLUMN users.name IS 'Имя';
COMMENT ON COLUMN users.login IS 'Логин';
COMMENT ON COLUMN users.password IS 'Пароль';