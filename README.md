# 🚀 Quasar Fire Operation API

## 📖 Descripción del Proyecto

**Quasar Fire Operation API** es una aplicación inspirada en el universo de Star Wars que simula un sistema de rescate espacial. El proyecto implementa un algoritmo de **trilateración** para determinar la ubicación exacta de una nave imperial en peligro basándose en las señales recibidas por tres satélites rebeldes.

## 🎯 Objetivo Principal

La API resuelve dos problemas fundamentales:
1. **Localización**: Determinar la posición exacta (coordenadas X, Y) de una nave en el espacio
2. **Comunicación**: Reconstruir el mensaje de socorro interceptado por los satélites

## 🌌 Contexto de la Historia

- **Escenario**: Una nave imperial está enviando una señal de socorro
- **Satélites**: Tres satélites rebeldes (`kenobi`, `skywalker`, `sato`) interceptan la señal
- **Problema**: Cada satélite recibe parte del mensaje y mide la distancia a la nave
- **Objetivo**: Determinar la ubicación exacta y reconstruir el mensaje completo

## 🛰️ Configuración de Satélites

Los tres satélites están posicionados en coordenadas fijas:

```java
- Kenobi:     (-500, -200)
- Skywalker:  (100,  -100)  
- Sato:       (500,   100)
```

## 📐 Algoritmo de Trilateración Lineal

### ¿Qué es la Trilateración?

La **trilateración** es un método matemático para determinar la posición de un punto usando las distancias conocidas desde tres puntos de referencia. Es similar al GPS que usamos en nuestros teléfonos.

### Principio Matemático

Cada satélite define un **círculo** centrado en su posición con radio igual a la distancia medida:
- **Círculo 1**: Centro en Kenobi, radio = distancia_1
- **Círculo 2**: Centro en Skywalker, radio = distancia_2  
- **Círculo 3**: Centro en Sato, radio = distancia_3

La nave está ubicada en el punto donde los **tres círculos se intersectan**.

### Fórmulas Matemáticas

#### 1. Sistema de Ecuaciones Base
Para cada satélite tenemos la ecuación de un círculo:
```
(x - x₁)² + (y - y₁)² = d₁²  // Satélite Kenobi
(x - x₂)² + (y - y₂)² = d₂²  // Satélite Skywalker  
(x - x₃)² + (y - y₃)² = d₃²  // Satélite Sato
```

#### 2. Linearización del Sistema
Restando la primera ecuación de la segunda y tercera:
```
2(x₂-x₁)x + 2(y₂-y₁)y = d₁² - d₂² - x₁² + x₂² - y₁² + y₂²
2(x₃-x₂)x + 2(y₃-y₂)y = d₂² - d₃² - x₂² + x₃² - y₂² + y₃²
```

#### 3. Sistema Lineal (Ax + By = C)
```
coefficientA·x + coefficientB·y = constantC
coefficientD·x + coefficientE·y = constantF
```

Donde:
- `coefficientA = 2(x₂ - x₁)`
- `coefficientB = 2(y₂ - y₁)`  
- `constantC = d₁² - d₂² - x₁² + x₂² - y₁² + y₂²`
- `coefficientD = 2(x₃ - x₂)`
- `coefficientE = 2(y₃ - y₂)`
- `constantF = d₂² - d₃² - x₂² + x₃² - y₂² + y₃²`

#### 4. Solución por Determinantes (Regla de Cramer)
```
x = (constantC·coefficientE - constantF·coefficientB) / (coefficientA·coefficientE - coefficientD·coefficientB)
y = (coefficientA·constantF - coefficientD·constantC) / (coefficientA·coefficientE - coefficientD·coefficientB)
```

### Implementación en el Código

```java
// Coeficientes lineales
float coefficientA = 2 * (p2[0] - p1[0]);
float coefficientB = 2 * (p2[1] - p1[1]);
float constantC = d1² - d2² - p1[0]² + p2[0]² - p1[1]² + p2[1]²;

float coefficientD = 2 * (p3[0] - p2[0]);
float coefficientE = 2 * (p3[1] - p2[1]);
float constantF = d2² - d3² - p2[0]² + p3[0]² - p2[1]² + p3[1]²;

// Solución
float x = (constantC*coefficientE - constantF*coefficientB) / (coefficientA*coefficientE - coefficientD*coefficientB);
float y = (coefficientA*constantF - coefficientD*constantC) / (coefficientA*coefficientE - coefficientD*coefficientB);
```

## 🎯 Algoritmo de Reconstrucción de Mensajes

### Problema
Cada satélite recibe un array de palabras con algunas posiciones vacías:
```
Kenobi:    ["este", "", "", "mensaje", ""]
Skywalker: ["", "es", "", "", "secreto"]
Sato:      ["este", "", "un", "", ""]
```

### Solución
El algoritmo combina la información de todos los satélites:

1. **Determinar longitud máxima** del mensaje
2. **Para cada posición**, tomar la primera palabra no vacía encontrada
3. **Filtrar** posiciones completamente vacías
4. **Unir** las palabras con espacios

**Resultado**: `"este es un mensaje secreto"`

### Implementación
```java
private String getMessage(List<List<String>> messages) {
    int maxLen = messages.stream().mapToInt(List::size).max().orElse(0);
    String[] result = new String[maxLen];
    
    for (int i = 0; i < maxLen; i++) {
        for (List<String> msgArr : messages) {
            if (i < msgArr.size() && msgArr.get(i) != null && !msgArr.get(i).isBlank()) {
                result[i] = msgArr.get(i);
                break;
            }
        }
        if (result[i] == null) result[i] = "";
    }
    
    return String.join(" ", Arrays.stream(result)
        .filter(s -> !s.isBlank())
        .toArray(String[]::new));
}
```

## 🏗️ Arquitectura del Proyecto

### Patrón Hexagonal (Clean Architecture)
```
📁 api/
├── 🎮 rest/              # Controladores REST (Adaptadores de entrada)
├── 💼 business/          # Lógica de negocio (Casos de uso)
├── 🔧 provider/          # Proveedores (Adaptadores de salida)
├── 📋 domain/            # Entidades y DTOs
├── ⚠️ exception/         # Manejo de excepciones
├── 🛠️ utils/            # Utilidades y constantes
└── ⚙️ config/           # Configuración (Swagger, Logging)
```

### Componentes Principales

#### 1. **REST Controllers**
- `TopSecretRest`: Endpoint `/top-secret` (Nivel 1)
- `TopSecretSplitRest`: Endpoints `/top-secret-split` (Nivel 3)

#### 2. **Business Layer**
- `MessageService`: Interface del servicio principal
- `MessageServiceImpl`: Implementación de la lógica de negocio

#### 3. **Provider Layer**
- `LocationProvider`: Interface para trilateración
- `LocationProviderImpl`: Implementación del algoritmo

#### 4. **Domain Objects**
- `TopSecretRequestDTO`: Request para nivel 1
- `SatelliteDTO`: Información de un satélite
- `ResponseDTO`: Respuesta con posición y mensaje
- `PositionDTO`: Coordenadas X, Y
- `SatelliteConfirmationDTO`: Confirmación de guardado split

#### 5. **Configuration**
- `SwaggerConfig`: Configuración de documentación OpenAPI
- `LoggingConfig`: Configuración de logging y información de startup

## 🚀 Endpoints de la API

**Base URL**: `http://localhost:8080/quasar-fire-operation`

### Nivel 1: `/top-secret`
**POST** `/quasar-fire-operation/top-secret`

Recibe información de los 3 satélites simultáneamente.

**Request Body:**
```json
{
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
}
```

**Response:**
```json
{
  "position": {
    "x": -58.315334,
    "y": -69.55141
  },
  "message": "este es un mensaje secreto"
}
```

### Nivel 3: `/top-secret-split`

#### POST `/quasar-fire-operation/top-secret-split/{satelliteName}`
Envía información de un satélite individual.

**Request Body:**
```json
{
  "distance": 100.0,
  "message": ["este", "", "", "mensaje", ""]
}
```

**Response:**
```json
{
  "message": "Información del satélite kenobi guardada exitosamente",
  "satelliteName": "kenobi",
  "instructions": "Los datos han sido almacenados. Envía información de los 3 satélites para obtener la ubicación."
}
```

#### GET `/quasar-fire-operation/top-secret-split`
Obtiene la posición y mensaje después de recibir información de los 3 satélites.

**Response:** (Igual que nivel 1)

## 🛠️ Tecnologías Utilizadas

- **Java 25** (Early Access) - Con características modernas
- **Spring Boot 3.5.6** - Framework principal
- **Spring Cloud 2024.0.0** - Para OpenFeign
- **Lombok** - Reducción de boilerplate
- **SLF4J** - Logging estructurado
- **SpringDoc OpenAPI** - Documentación automática
- **JUnit 5 + Mockito** - Testing
- **Gradle** - Gestión de dependencias

## 🔧 Configuración y Ejecución

### Prerrequisitos
- Java 25 (Early Access)
- Gradle 8.x

### Ejecución Local
```bash
# Compilar
./gradlew build

# Ejecutar
./gradlew bootRun

# La API estará disponible en: http://localhost:8080/quasar-fire-operation
```

### Documentación Swagger
Una vez ejecutando, accede a:
- **Swagger UI**: http://localhost:8080/quasar-fire-operation/swagger-ui.html
- **OpenAPI JSON**: http://localhost:8080/quasar-fire-operation/v3/api-docs

## 🧪 Ejemplo de Uso

### Caso Práctico
Una nave imperial está en posición `(-58.31, -69.55)` y envía el mensaje `"este es un mensaje secreto"`.

Los satélites detectan:
- **Kenobi**: distancia 100.0, mensaje ["este", "", "", "mensaje", ""]
- **Skywalker**: distancia 115.5, mensaje ["", "es", "", "", "secreto"]  
- **Sato**: distancia 142.7, mensaje ["este", "", "un", "", ""]

**Resultado**: La API calcula correctamente la posición y reconstruye el mensaje.

## 📊 Casos de Error

### Errores de Localización
- **Información insuficiente**: Menos de 3 satélites
- **Posiciones colineales**: Satélites en línea recta
- **División por cero**: Determinante del sistema = 0
- **Configuración inválida**: Determinante muy pequeño

### Errores de Mensaje
- **Mensaje vacío**: No se puede reconstruir
- **Información inconsistente**: Conflictos entre satélites

### Errores de Validación
- **Argumentos inválidos**: Datos de entrada incorrectos
- **Satélite no reconocido**: Nombres de satélites inválidos

## 🏆 Características Destacadas

### Buenas Prácticas Implementadas
- ✅ **Arquitectura Hexagonal** - Separación clara de responsabilidades
- ✅ **SOLID Principles** - Código mantenible y extensible
- ✅ **Design Patterns** - Factory, Strategy, Dependency Injection
- ✅ **Clean Code** - Métodos pequeños, nombres descriptivos
- ✅ **Documentation** - JavaDoc completo
- ✅ **Error Handling** - Manejo robusto de excepciones
- ✅ **Logging Completo** - Logs estructurados en todos los niveles
- ✅ **Testing Ready** - Estructura preparada para pruebas
- ✅ **Java 25 Features** - Aprovecha las últimas características del lenguaje

### Optimizaciones
- **Concurrent Collections** - Para el almacenamiento split thread-safe
- **Stream API** - Procesamiento funcional moderno
- **Lombok** - Reducción de código repetitivo
- **Structured Logging** - Logs informativos con niveles apropiados
- **Immutable Collections** - Constantes inmutables y thread-safe
- **Factory Methods** - Creación consistente de objetos de respuesta

### Logging y Monitoreo
- **Logging por capas** - Niveles específicos por paquete
- **Archivos rotativos** - Logs persistentes con rotación automática
- **Endpoints de monitoreo** - Health checks y métricas
- **Patrones coloridos** - Logs de desarrollo fáciles de leer
- **Información de startup** - Detalles completos al iniciar la aplicación

## 🎯 Conclusión

Esta API demuestra cómo aplicar conceptos matemáticos complejos (trilateración) en un contexto de programación moderno, utilizando las mejores prácticas de desarrollo de software y tecnologías de vanguardia como Java 25 y Spring Boot 3.5.

El proyecto no solo resuelve el problema técnico planteado, sino que lo hace de manera elegante, mantenible y escalable, siguiendo principios de arquitectura limpia y desarrollo profesional, con un sistema completo de logging y monitoreo para entornos de producción.

---

**¡Que la fuerza te acompañe en tu misión de rescate!** 🌟
