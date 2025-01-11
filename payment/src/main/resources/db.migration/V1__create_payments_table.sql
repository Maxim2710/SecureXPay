CREATE TABLE payments (
        id SERIAL PRIMARY KEY, -- Уникальный идентификатор платежа
        user_id BIGINT NOT NULL, -- ID пользователя, связанного с платежом
        amount DECIMAL(15, 2) NOT NULL, -- Сумма платежа
        status VARCHAR(50) NOT NULL, -- Статус платежа (например, 'PENDING', 'CONFIRMED', 'FAILED')
        otp VARCHAR(6), -- Одноразовый пароль для подтверждения платежа
        created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL, -- Дата создания записи
        updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL, -- Дата последнего обновления записи
        FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE -- Связь с таблицей users
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
    BEFORE UPDATE ON payments
    FOR EACH ROW
EXECUTE FUNCTION update_updated_at_column();
