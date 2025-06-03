FROM maven:3.9.6-eclipse-temurin-21 AS build

WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests && ls -lh target

# Финальный образ
FROM eclipse-temurin:21-jre

ENV JAVA_TOOL_OPTIONS="-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005"

WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080
EXPOSE 5005

ENTRYPOINT ["java", "-jar", "app.jar"]