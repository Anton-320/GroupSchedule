# Используем официальный образ с OpenJDK 17 в качестве базового
FROM openjdk:17-jdk-slim

# Устанавливаем рабочую директорию внутри контейнера
WORKDIR /app

# Копируем файл конфигурации проекта Maven
COPY pom.xml .

# Копируем скрипт maven wrapper
COPY .mvn/ .mvn
COPY mvnw .

# Скачиваем зависимости проекта
RUN ./mvnw dependency:resolve

# Копируем исходный код проекта
COPY src ./src

# Сборка проекта
RUN ./mvnw clean package -DskipTests

# Указываем порт, который будет использоваться приложением
EXPOSE 8080

# Указываем переменные окружения
ENV SPRING_PROFILES_ACTIVE prod

# Команда для запуска приложения
CMD ["java", "-jar", "target/GroupSchedule-0.0.1-SNAPSHOT.jar"]