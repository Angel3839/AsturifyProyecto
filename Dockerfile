# Usa una imagen de OpenJDK
FROM openjdk:17-jdk-slim

# Crea el directorio para la app
WORKDIR /app

# Copia el jar
COPY target/tarea3dwesangel-0.0.1-SNAPSHOT.jar app.jar

# Expón el puerto (Render usará la variable $PORT)
EXPOSE 8080

# Comando para ejecutar
CMD ["java", "-Dserver.port=$PORT", "-jar", "app.jar"]
