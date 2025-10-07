## Microservices Homework

### Описание проекта
Микросервисная система для управления клиентами, продуктами, счетами, картами, платежами и транзакциями.
Проект разделён на четыре независимых модуля:

* Client Processing – управление пользователями, клиентами и продуктами
* Account Processing – управление счетами, картами, платежами и транзакциями
* Credit Processing – управление кредитными продуктами и платежными регистрами
* Utility – модуль с DTO, enum'ами и общими классами, которые переиспользуются другими сервисами

Проект учебный, разработан по ТЗ в рамках изучения Spring Boot 3, микросервисной архитектуры и работы с БД.

### Стэк проекта
* Java 21
* Spring Boot (Web, Data JPA, Validation)
* PostgreSQL
* Liquibase
* Maven

### Содержание:
1. [Техническое задание](#техническое-задание)
2. [Инструкция по запуску проекта](#инструкция-по-установке-и-запуску-проекта)
3. [Автор](#автор)

### Техническое задание
* [Тех-задание](docs/TZ.txt)

### Инструкция по установке и запуску проекта
1) Установите Docker Desktop
2) Запуск всего проекта через Docker Compose:
```
docker compose up --build -d
```
3) Сервисы после старта:
- MS-1: http://localhost:8080
- MS-2: http://localhost:8081
- MS-3: http://localhost:8082
- PostgreSQL: localhost:5432 (user=postgres, password=password)
- Kafka: localhost:9092 (внутри compose — kafka:9092)

### Краткое описание модулей и конфигурации
- client-processing: регистрация клиента, CRUD продуктов, CRUD портфеля + отправка в Kafka (`client_products`, `client_credit_products`, `client_cards`).
- account-processing: слушает Kafka, создаёт `Account` и `Card`, логирует транзакции.
- credit-processing: слушает `client_credit_products`, делает GET в MS‑1, принимает решение по лимиту, создаёт `ProductRegistry` и график `PaymentRegistry`.
- utility: DTO и enum.

Основные настройки:
- Kafka: `t1.kafka.*` в `application.yml` каждого модуля (bootstrap `localhost:9092`).
- БД и Liquibase: `db/changelog/*` и `spring.datasource.*`.
- MS‑3: `t1.credit.*` и `integration.ms1.base-url`.

### Тестирование (Postman)
1) Импортируйте окружение: `postman/MicroservicesHomework.postman_environment.json`
2) Импортируйте коллекцию: `postman/MicroservicesHomework.postman_collection.json`
3) Выберите окружение `Local` и выполните запросы по порядку:
- Clients: Register
- Products: Create
- ClientProducts: Create (Deposit/Card → счёт; Credit → кредитный продукт и график)
- Cards: Create

### Автор
* Latyshev Danila
