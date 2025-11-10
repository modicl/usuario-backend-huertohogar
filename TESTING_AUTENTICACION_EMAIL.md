# 📝 Ejemplos para Testear - Autenticación con Email

## ✅ Cambios Realizados

- ✅ Autenticación por **EMAIL** (no por ID)
- ✅ JWT token generado automáticamente
- ✅ Respuesta con datos completos del usuario

---

## 🧪 Ejemplo 1: Crear Usuario

**Primero, crea un usuario** (si no tienes uno):

### Request (Postman)

```
POST http://localhost:8080/api/v1/usuarios
Content-Type: application/json
```

### Body

```json
{
  "pNombre": "Luis",
  "sNombre": "Andrés",
  "aPaterno": "González",
  "aMaterno": "Ramírez",
  "email": "luis@example.com",
  "telefono": "+56987654321",
  "direccion": "Calle Prueba 123",
  "password": "MiPassword123!"
}
```

### Response (201 Created)

```json
{
  "idUsuario": 1,
  "pNombre": "Luis",
  "sNombre": "Andrés",
  "aPaterno": "González",
  "aMaterno": "Ramírez",
  "email": "luis@example.com",
  "telefono": "+56987654321",
  "direccion": "Calle Prueba 123",
  "passwordHashed": "$2a$10$Hj8pzH.L9K2yYvZmRt5QdeJ7mKpLj9QvZ7Y8X2W1V0U9T8S7R6Q5P"
}
```

---

## 🔐 Ejemplo 2: Autenticar Usuario (LO NUEVO)

**Ahora usa EMAIL para autenticar:**

### Request (Postman)

```
POST http://localhost:8080/api/v1/usuarios/authenticate
Content-Type: application/json
```

### Body (⭐ NUEVO - Usa EMAIL, no ID)

```json
{
  "email": "luis@example.com",
  "password": "MiPassword123!"
}
```

### Response (200 OK) - ✅ Recibe JWT Token

```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwiZW1haWwiOiJsdWlzQGV4YW1wbGUuY29tIiwiaWF0IjoxNzMxMjI3ODAwLCJleHAiOjE3MzEzMTQyMDB9.8aB9cD2eF3gH4jI5kL6mN7oP8qR9sT0uV1wX2yZ3aB4",
  "idUsuario": 1,
  "email": "luis@example.com",
  "pNombre": "Luis",
  "aPaterno": "González"
}
```

**Guarda el token en localStorage:**

```javascript
localStorage.setItem('token', 'eyJhbGciOiJIUzI1NiJ9...');
```

---

## 🔴 Ejemplo 3: Error - Credenciales Inválidas

### Request

```
POST http://localhost:8080/api/v1/usuarios/authenticate
Content-Type: application/json
```

### Body (Email o contraseña incorrectos)

```json
{
  "email": "luis@example.com",
  "password": "WrongPassword123!"
}
```

### Response (401 Unauthorized)

```
"Credenciales inválidas"
```

---

## 🔴 Ejemplo 4: Error - Email no existe

### Request

```
POST http://localhost:8080/api/v1/usuarios/authenticate
Content-Type: application/json
```

### Body

```json
{
  "email": "noexiste@example.com",
  "password": "MiPassword123!"
}
```

### Response (401 Unauthorized)

```
"Credenciales inválidas"
```

---

## 📱 Ejemplo 5: Usar el Token en React

```javascript
// 1. Login y obtener token
const response = await fetch('http://localhost:8080/api/v1/usuarios/authenticate', {
  method: 'POST',
  headers: { 'Content-Type': 'application/json' },
  body: JSON.stringify({
    email: 'luis@example.com',
    password: 'MiPassword123!'
  })
});

const data = await response.json();
// { token: "eyJhbGc...", idUsuario: 1, email: "luis@example.com", ... }

// 2. Guardar token
localStorage.setItem('token', data.token);
localStorage.setItem('usuario', JSON.stringify(data));

// 3. Usar token en próximas peticiones
const token = localStorage.getItem('token');

const response2 = await fetch('http://localhost:8080/api/v1/usuarios', {
  method: 'GET',
  headers: {
    'Content-Type': 'application/json',
    'Authorization': `Bearer ${token}`  // ⭐ Usar token aquí
  }
});
```

---

## 📋 Resumen de Cambios

| Aspecto | Antes | Ahora |
|---------|-------|-------|
| **Identificador** | ID numérico | Email |
| **Request Body** | `{ "idUsuario": 1, "password": "..." }` | `{ "email": "user@example.com", "password": "..." }` |
| **Response** | String de texto | JSON con token JWT |
| **Token** | ❌ No incluido | ✅ Incluido en respuesta |
| **Datos Usuario** | ❌ No incluidos | ✅ Incluidos en respuesta |

---

## ✅ Checklist de Prueba

Copia este checklist para validar que todo funciona:

```
□ Crear usuario con contraseña válida (POST /usuarios)
□ Intentar autenticar con EMAIL correcto y PASSWORD correcto
  → Esperado: 200 OK con token JWT
□ Intentar autenticar con EMAIL correcto y PASSWORD incorrecto
  → Esperado: 401 Unauthorized
□ Intentar autenticar con EMAIL inexistente
  → Esperado: 401 Unauthorized
□ Guardar token en localStorage
□ Usar token en header Authorization Bearer en próximas peticiones
```

---

## 🚀 Próximos Pasos

1. **Implementar Guard en React** para redirigir si no hay token
2. **Interceptor HTTP** para agregar token automáticamente
3. **Refresh Token** para extender sesión
4. **Logout** para limpiar token

---

**Versión:** 2.0.1  
**Fecha:** 2025-11-09  
**Estado:** ✅ Listo para testear
