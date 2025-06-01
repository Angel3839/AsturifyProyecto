# Etapa 1: construir la aplicación
FROM maven:3.9.6-eclipse-temurin-17 as build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Etapa 2: ejecutar la app
FROM eclipse-temurin:17-jdk
WORKDIR /app
COPY --from=build /app/target/tarea3dwesangel-0.0.1-SNAPSHOT.jar app.jar

# Render usa una variable de entorno PORT que Spring debe respetar
ENV PORT=8080
EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
