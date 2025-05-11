# Бэкенд для приложения "Чат и блог"

## Краткое описание:
Данный проект представляет собой бэкенд для приложения "Чат и блог", реализованного с использованием Spring Boot. Он включает в себя несколько микросервисов, каждый из которых отвечает за свою часть функционала (chat, post, user).

## Установка
1. Клонируйте репозиторий
2. Каждый микросервис надо собрать в Docker image: `./mvnw spring-boot:build-image`
3. Запустите все микросервисы с помощью Docker Compose: `docker-compose up`

## Микросервисы:
1. Для авторизации используется Keycloak. 
2. Для работы с Keycloak предусмотрен user-service
3. Для работы с постами предусмотрен post-service
4. Для работы с чатом предусмотрен chat-service
5. Для service discovery предусмотрен сервер Eureka (netflix eureka)
6. Для API Gateway предусмотрен api-gateway (spring cloud gateway reactive)
7. Для конфигурации сервисов предусмотрен config-service (spring cloud config)

## Используемые технологии:
- Spring Boot
- Spring Cloud
- Spring Security
- Spring Data JPA
- Spring WebSocket
- Spring MVC
- Docker
- PostgreSQL
- Keycloak
- Spring Cloud Gateway
- Spring Cloud Config
- Spring Cloud Eureka
- Spring Cloud Circuit Breaker (Resilience4j)
- Spring Cloud OpenFeign
- Spring OpenAPI

## Идеи для улучшения:
- Собрать все логи в один сервис (например, ELK stack)
- Добавить мониторинг (например, Prometheus + Grafana)
- В chat-service перейти с SimpleMessageBroker на RabbitMQ/Kafka
- Сделать фронтенд
- Продокументировать chat-service и user-service с помощью OpenAPI
- Развернуть все