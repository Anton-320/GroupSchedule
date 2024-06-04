FROM maven:3.9.2-eclipse-temurin-17-alpine AS builder

COPY . .
RUN mvn clean package -DskipTests

FROM openjdk:17
COPY --from=builder target/*.jar app.jar
EXPOSE 8080
CMD ["java","-jar","app.jar"]