# Contexto del Proyecto - Generado con GEMINI CLI

## Descripción General
**Proyecto:** Usuario Backend - Huerto Hogar
**Tipo:** Microservicio REST API (Spring Boot)
**Propósito:** Gestión de usuarios, autenticación (JWT + BCrypt), y administración de perfiles para el ecosistema Huerto Hogar.

## 🛠 Stack Tecnológico
*   **Lenguaje:** Java 21 (OpenJDK)
*   **Framework:** Spring Boot 3.5.7
*   **Build Tool:** Maven (Wrapper incluido: `./mvnw`)
*   **Base de Datos:**
    *   Desarrollo Local: MySQL 8.0 (vía Docker Compose)
    *   Driver: Soporte configurado para PostgreSQL y MySQL (revisar `pom.xml` y `application.properties`).
*   **Seguridad:** Spring Security, BCrypt, JWT (jjwt).
*   **Documentación API:** Swagger UI / OpenAPI (`/swagger-ui.html`).

## 🚀 Comandos Principales

### Construcción y Ejecución
```bash
# Compilar el proyecto
./mvnw clean package

# Ejecutar localmente
./mvnw spring-boot:run

# Ejecutar con Docker Compose (Base de datos + App)
docker-compose up --build
```

### Testing
```bash
# Ejecutar tests unitarios
./mvnw test

# Generar reporte de cobertura (JaCoCo)
./mvnw test jacoco:report
```
*Consulte `TESTING_GUIDE.md` para scripts de prueba manual con `curl`.*

## 📂 Estructura del Proyecto
La lógica principal reside en `src/main/java/cl/huertohogar/usuario_backend/`:

*   `config/`: Configuraciones de seguridad (JWT, CORS), OpenAPI y excepciones globales.
*   `controller/`: Endpoints REST (`UsuarioController`, `CiudadController`, etc.).
*   `dto/`: Objetos de transferencia de datos (Request/Response).
*   `model/`: Entidades JPA (`Usuario`, `Ciudad`, `Region`, `Orden`).
*   `repository/`: Interfaces Spring Data JPA.
*   `service/`: Lógica de negocio y validaciones.
*   `util/`: Utilidades como `JwtUtil`.

## ⚙️ Configuración
El archivo `src/main/resources/application.properties` define la configuración base.
*   **Base de Datos:** Utiliza variables de entorno (`DB_URL`, `DB_USERNAME`, `DB_PASSWORD`) o valores por defecto definidos en `docker-compose.yml`.
*   **JWT:** La clave secreta y expiración están definidas (nota: la clave actual es un placeholder de desarrollo).

## 📝 Convenciones y Notas
*   **Validaciones:** Se utiliza Jakarta Validation en DTOs y Entidades.
*   **Manejo de Errores:** `GlobalExceptionHandler` captura excepciones y retorna respuestas JSON estandarizadas.
*   **Refactorización Reciente:** La gestión de contraseñas se movió a la entidad `Usuario` (campo `passwordHashed`), eliminando la tabla `password` antigua (ver `README.md` v2.0.0).

## 🔗 Enlaces Útiles (Local)
*   Swagger UI: http://localhost:8080/swagger-ui.html
*   API Docs: http://localhost:8080/v3/api-docs
