# Etapa 1: Build
FROM maven:3.9.5-eclipse-temurin-21 AS builder

WORKDIR /app

# Copiar archivos de configuración de Maven
COPY pom.xml .

# Descargar dependencias (layer cacheable)
RUN mvn dependency:go-offline -B

# Copiar código fuente
COPY src ./src

# Compilar y empaquetar
RUN mvn clean package -DskipTests -q

# Etapa 2: Runtime (imagen más pequeña)
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# Instalar herramientas útiles
RUN apk add --no-cache curl

# Copiar JAR desde la etapa de build
COPY --from=builder /app/target/*.jar app.jar

# Crear usuario no-root
RUN addgroup -g 1000 app && \
    adduser -D -u 1000 -G app app && \
    chown -R app:app /app

USER app

# Exponer puerto (por defecto Spring Boot usa 8080)
EXPOSE 8080

# Health check
HEALTHCHECK --interval=30s --timeout=10s --start-period=5s --retries=3 \
    CMD curl -f http://localhost:8080/actuator/health || exit 1

# Comando para ejecutar la aplicación
ENTRYPOINT ["java", "-jar", "app.jar"]

# Variables de entorno por defecto
ENV JAVA_OPTS="-Xmx512m -Xms256m"
