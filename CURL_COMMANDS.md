# 🚀 Comandos cURL para Quasar Fire Operation API

## 📋 Endpoints Disponibles

La API está disponible en: `http://localhost:8080/quasar-fire-operation`

---

## 1️⃣ **Endpoint Nivel 1: POST /top-secret**

### 🎯 Descripción
Recibe información de los 3 satélites simultáneamente y devuelve la posición y mensaje decodificado.

### 📡 Comando cURL
```bash
curl -X POST "http://localhost:8080/quasar-fire-operation/top-secret" \
  -H "Content-Type: application/json" \
  -H "Accept: application/json" \
  -d '{
    "satellites": [
      {
        "name": "kenobi",
        "distance": 100.0,
        "message": ["este", "", "", "mensaje", ""]
      },
      {
        "name": "skywalker",
        "distance": 115.5,
        "message": ["", "es", "", "", "secreto"]
      },
      {
        "name": "sato",
        "distance": 142.7,
        "message": ["este", "", "un", "", ""]
      }
    ]
  }'
```

### 📋 Respuesta Esperada
```json
{
  "position": {
    "x": -58.315334,
    "y": -69.55141
  },
  "message": "este es un mensaje secreto"
}
```

---

## 2️⃣ **Endpoint Nivel 3: POST /top-secret-split/{satelliteName}**

### 🎯 Descripción
Envía información de un satélite individual. Debes enviar datos de los 3 satélites antes de poder hacer el GET.

### 📡 Comandos cURL

#### 🛰️ Satélite Kenobi
```bash
curl -X POST "http://localhost:8080/quasar-fire-operation/top-secret-split/kenobi" \
  -H "Content-Type: application/json" \
  -H "Accept: application/json" \
  -d '{
    "distance": 100.0,
    "message": ["este", "", "", "mensaje", ""]
  }'
```

#### 🛰️ Satélite Skywalker
```bash
curl -X POST "http://localhost:8080/quasar-fire-operation/top-secret-split/skywalker" \
  -H "Content-Type: application/json" \
  -H "Accept: application/json" \
  -d '{
    "distance": 115.5,
    "message": ["", "es", "", "", "secreto"]
  }'
```

#### 🛰️ Satélite Sato
```bash
curl -X POST "http://localhost:8080/quasar-fire-operation/top-secret-split/sato" \
  -H "Content-Type: application/json" \
  -H "Accept: application/json" \
  -d '{
    "distance": 142.7,
    "message": ["este", "", "un", "", ""]
  }'
```

### 📋 Respuesta Esperada (para todos los POST)
```json
{
  "message": "Información del satélite kenobi guardada exitosamente",
  "satelliteName": "kenobi",
  "instructions": "Los datos han sido almacenados. Envía información de los 3 satélites para obtener la ubicación."
}
```

**Nota**: El campo `satelliteName` en la respuesta reflejará el nombre del satélite específico que enviaste (kenobi, skywalker, o sato).

---

## 3️⃣ **Endpoint Nivel 3: GET /top-secret-split**

### 🎯 Descripción
Obtiene la posición y mensaje después de haber enviado información de los 3 satélites mediante los POST anteriores.

### 📡 Comando cURL
```bash
curl -X GET "http://localhost:8080/quasar-fire-operation/top-secret-split" \
  -H "Accept: application/json"
```

### 📋 Respuesta Esperada
```json
{
  "position": {
    "x": -58.315334,
    "y": -69.55141
  },
  "message": "este es un mensaje secreto"
}
```

---

## 🧪 **Secuencia de Pruebas Completa**

### 📋 Opción 1: Nivel 1 (Directo)
```bash
# 1. Prueba directa con todos los satélites
curl -X POST "http://localhost:8080/quasar-fire-operation/top-secret" \
  -H "Content-Type: application/json" \
  -d '{
    "satellites": [
      {"name": "kenobi", "distance": 100.0, "message": ["este", "", "", "mensaje", ""]},
      {"name": "skywalker", "distance": 115.5, "message": ["", "es", "", "", "secreto"]},
      {"name": "sato", "distance": 142.7, "message": ["este", "", "un", "", ""]}
    ]
  }'
```

### 📋 Opción 2: Nivel 3 (Secuencial)
```bash
# 1. Enviar datos del satélite Kenobi
curl -X POST "http://localhost:8080/quasar-fire-operation/top-secret-split/kenobi" \
  -H "Content-Type: application/json" \
  -d '{"distance": 100.0, "message": ["este", "", "", "mensaje", ""]}'

# 2. Enviar datos del satélite Skywalker
curl -X POST "http://localhost:8080/quasar-fire-operation/top-secret-split/skywalker" \
  -H "Content-Type: application/json" \
  -d '{"distance": 115.5, "message": ["", "es", "", "", "secreto"]}'

# 3. Enviar datos del satélite Sato
curl -X POST "http://localhost:8080/quasar-fire-operation/top-secret-split/sato" \
  -H "Content-Type: application/json" \
  -d '{"distance": 142.7, "message": ["este", "", "un", "", ""]}'

# 4. Obtener resultado final
curl -X GET "http://localhost:8080/quasar-fire-operation/top-secret-split" \
  -H "Accept: application/json"
```

---

## 🎯 **Casos de Prueba Adicionales**

### ❌ Error: Información Insuficiente
```bash
# Intentar GET sin enviar todos los satélites
curl -X GET "http://localhost:8080/quasar-fire-operation/top-secret-split" \
  -H "Accept: application/json"
```

**Respuesta esperada:**
```json
{
  "error": "Información insuficiente de satélites."
}
```

### ❌ Error: Datos Inválidos
```bash
# Enviar solo 2 satélites en nivel 1
curl -X POST "http://localhost:8080/quasar-fire-operation/top-secret" \
  -H "Content-Type: application/json" \
  -d '{
    "satellites": [
      {"name": "kenobi", "distance": 100.0, "message": ["este"]},
      {"name": "skywalker", "distance": 115.5, "message": ["es"]}
    ]
  }'
```

---

## 📋 **Notas Importantes**

1. **Orden de ejecución**: Para el nivel 3, debes ejecutar los 3 POST antes del GET
2. **Context Path**: Todos los endpoints incluyen `/quasar-fire-operation`
3. **Content-Type**: Siempre usar `application/json` para POST
4. **Headers**: Accept header recomendado para respuestas JSON
5. **Datos de prueba**: Los valores de distancia y mensajes son de ejemplo

---

## 🛠️ **Herramientas Alternativas**

### 📋 Con HTTPie
```bash
# Nivel 1
http POST localhost:8080/quasar-fire-operation/top-secret \
  satellites:='[
    {"name": "kenobi", "distance": 100.0, "message": ["este", "", "", "mensaje", ""]},
    {"name": "skywalker", "distance": 115.5, "message": ["", "es", "", "", "secreto"]},
    {"name": "sato", "distance": 142.7, "message": ["este", "", "un", "", ""]}
  ]'
```

### 📋 Con Postman
1. **URL**: `http://localhost:8080/quasar-fire-operation/top-secret`
2. **Method**: POST
3. **Headers**: `Content-Type: application/json`
4. **Body**: Raw JSON con el payload de ejemplo

---

**¡Que la fuerza te acompañe en las pruebas!** 🌟
