# Получение расписания группы БГУИР
Сервис должен принимать номер группы и дату и возвращать расписание для заданной группы для заданной даты
* результаты можно запрашивать из открытого API https://iis.bsuir.by/api
## Задание 1 - Basic REST service
1. Создать и запустить локально простейший веб/REST сервис, используя любой открытый пример с использованием Java stack: Spring (Spring Boot)/maven/gradle/Jersey/ Spring MVC.
2. Добавить GET ендпоинт, принимающий входные параметры в качестве queryParams в URL согласно варианту, и возвращающий любой hard-coded результат в виде JSON согласно варианту.
## Задание 2 - JPA (Hibernate/Spring Data)
1. Подключить в проект БД (PostgreSQL/MySQL/и т.д.).
(0 - 7 баллов) - Реализация связи один ко многим @OneToMany
(8 - 10 баллов) - Реализация связи многие ко многим @ManyToMany
2. Реализовать CRUD-операции со всеми сущностями.