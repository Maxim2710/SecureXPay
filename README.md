# ✨ Система управления безопасными платежами (SecureXPay) ✨

![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.3.5-green) ![Java](https://img.shields.io/badge/Java-21-orange) ![PostgreSQL](https://img.shields.io/badge/PostgreSQL-16-blue) ![JWT](https://img.shields.io/badge/JWT-Secure-yellowgreen) ![Swagger](https://img.shields.io/badge/Swagger-OpenAPI-informational) ![Docker](https://img.shields.io/badge/Docker-Compose-blue)

💳 Добро пожаловать в **Систему управления безопасными платежами**! Этот проект состоит из двух микросервисов, предназначенных для обеспечения безопасной обработки платежей и аутентификации пользователей с использованием 3D-Secure.

---

## 🏗️ **Структура проекта**

### 📦 **Микросервисы**

1. **Сервис аутентификации**
   - 🔒 Управляет регистрацией, входом в систему и управлением пользователями.
   - 🛡️ Обеспечивает аутентификацию на основе JWT.

2. **Сервис платежей**
   - 💰 Обрабатывает платежи, генерирует OTP для подтверждения и хранит историю платежей.

---

## ✨ **Функционал**

### 🔑 **Сервис аутентификации**
- 📝 Регистрация пользователей с проверкой данных.
- 🔑 Аутентификация пользователей на основе JWT.
- 📧 Сброс пароля через электронную почту.
- ⚙️ Управление профилем пользователя.

### 💳 **Сервис платежей**
- 🏦 Создание платежей и подтверждение через OTP.
- 🚦 Управление статусами: `PENDING`, `CONFIRMED`, `CANCELED` и др.
- 🔄 Функционал возвратов и отмены платежей.
- 📜 Просмотр истории платежей для авторизованных пользователей.
- ✉️ Уведомления по электронной почте для OTP и подтверждений.

---

## ⚙️ **Используемые технологии**

### **Backend**
- **Java 21**
- **Spring Boot** (разработка REST API)
- **Spring Security + JWT** (аутентификация и авторизация)
- **Spring Data JPA** (взаимодействие с базой данных)
- **Spring Data JDBC**
- **Spring MVC**
- **Java Mail API** (отправка электронной почты)
- **HMAC-based OTP (Time-based)** (генерация OTP)
- **Swagger/OpenAPI** (документация API)
- **OpenFeign** (интеграция между сервисами)
- **JAXB API**
- **Lombok**

### **База данных**
- **PostgreSQL**

---

## 📚 **Обзор конечных точек**

### 🔐 **Сервис аутентификации**

| Метод  | Конечная точка         | Описание                        |
|--------|-----------------------|----------------------------------|
| POST   | `/auth/register`      | 📝 Регистрация нового пользователя.|
| POST   | `/auth/login`         | 🔑 Аутентификация и выдача JWT.    |
| POST   | `/auth/reset-password`| 📧 Запрос на сброс пароля.         |
| PUT    | `/auth/update-password`| 🔒 Обновление пароля пользователя. |

### 💳 **Сервис платежей**

| Метод  | Конечная точка           | Описание                        |
|--------|--------------------------|----------------------------------|
| POST   | `/payments/create`       | 🏦 Создание нового платежа.        |
| POST   | `/payments/confirm`      | 🔐 Подтверждение платежа через OTP.|
| POST   | `/payments/cancel`       | 🚫 Отмена ожидающего платежа.      |
| POST   | `/payments/refund`       | 🔄 Возврат подтвержденного платежа.|
| GET    | `/payments/history`      | 📜 Получение истории платежей.     |
| GET    | `/payments/status/{id}`  | 🚦 Проверка статуса платежа по ID. |

---

## 🛠️ **Инструкции по настройке**

### Предварительные требования
- ☕ Java 21
- 🛠️ Maven
- 🐘 PostgreSQL

### **Шаг 1: Клонирование репозитория**
```bash
git clone https://github.com/Maxim2710/SecureXPay.git
cd SecureXPay
```

### **Шаг 2: Настройка базы данных**
Создайте базы данных PostgreSQL для сервисов:

```sql
CREATE DATABASE auth_service_db;
CREATE DATABASE payment_service_db;
```

Обновите файлы `application.yml` в каждом сервисе, указав свои учетные данные для базы данных.

### **Шаг 3: Сборка и запуск**

Перейдите в директорию каждого сервиса (`auth-service` и `payment-service`) и выполните:

```bash
mvn spring-boot:run
```

### **Шаг 4: Запуск с использованием Docker Compose**

В корне проекта находится файл `docker-compose.yml`. Для запуска всех сервисов выполните:

```bash
docker-compose up --build
```

### **Шаг 5: Доступ к документации API**
Откройте Swagger UI в браузере:
- 🔐 Сервис аутентификации: `http://localhost:8080/swagger-ui.html`
- 💳 Сервис платежей: `http://localhost:8081/swagger-ui.html`

---

## 🔒 **Основные аспекты безопасности**
- **Аутентификация через JWT** обеспечивает безопасные пользовательские сессии.
- **OTP на основе времени** добавляет дополнительный уровень подтверждения платежей.
- Ролевое управление доступом для выполнения критически важных действий.

---

## 🤝 **Как внести вклад**
Мы рады любым вашим предложениям! Пожалуйста, открывайте issue или отправляйте pull request.

---

## 📬 **Контакты**
Если у вас есть вопросы, свяжитесь с нами:
- 📧 **Электронная почта:** pm2710@mail.ru
- 🐙 **GitHub Issues**
