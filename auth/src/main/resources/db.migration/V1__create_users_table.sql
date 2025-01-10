CREATE TABLE users (
                       id SERIAL PRIMARY KEY, -- Уникальный идентификатор пользователя
                       email VARCHAR(320) NOT NULL UNIQUE, -- Email пользователя
                       username VARCHAR(50) NOT NULL,
                       hashed_password VARCHAR(255) NOT NULL, -- Хэшированный пароль
                       roles VARCHAR(255) NOT NULL, -- Роли пользователя (например, 'USER,ADMIN')
                       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL, -- Дата создания записи
                       updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL -- Дата последнего обновления записи
);

-- Функция для автоматического обновления updated_at
CREATE OR REPLACE FUNCTION update_updated_at_column()
    RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Триггер для обновления updated_at
CREATE TRIGGER set_updated_at
    BEFORE UPDATE ON users
    FOR EACH ROW
EXECUTE FUNCTION update_updated_at_column();
