# appGrupo1Productor - Microservicio Productor RabbitMQ

Evaluacion T1 del curso **Desarrollo de Aplicaciones Web II**  
Instituto Superior Tecnologico Cibertec  
Grupo 1

---

## Integrantes del Grupo

| N° | Apellidos y Nombres | Grupo |
|:--:|---------------------|:-----:|
| 1 | Chaupis Alvarez Jhonny Samuel | 1 |
| 2 | Cruz Valdez Ronald Corwin | 1 |
| 3 | Hinojosa Cano Carlos Daniel | 1 |
| 4 | Hurtado Sernaque Brayan Luis | 1 |
| 5 | Alayo Oliveros Mathias Miller | 1 |

---

## Descripcion del Proyecto

El microservicio productor corresponde a la solucion de la Pregunta 4 de la Evaluacion T1. Su funcion principal es exponer un endpoint REST que recibe una serie de posiciones numericas delimitadas por punto y coma y publicarlas asincronamente hacia un broker de mensajeria RabbitMQ para su posterior procesamiento por el servicio consumidor.

---

## Entorno y Requisitos Tecnicos

- **Lenguaje:** Java 25
- **Framework:** Spring Boot 4.1.1
- **Sistema de mensajeria:** RabbitMQ (protocolo AMQP 0-9-1)
- **Gestor de construccion:** Apache Maven 3.9+ (o Maven Wrapper incluido)
- **Puerto del servicio:** 8081
- **Puerto de RabbitMQ:** 5672 (puerto AMQP), 15672 (consola web de administracion)

---

## Diagrama de Secuencia

```mermaid
sequenceDiagram
    autonumber
    actor Cliente as Cliente HTTP
    participant Productor as appGrupo1Productor (Puerto 8081)
    participant Exchange as Exchange: Grupo1Exchange
    participant Queue as Queue: Grupo1Queue
    participant Consumidor as appGrupo1Consumidor (Puerto 8082)

    Cliente->>Productor: GET /api/fibonacci/send?numbers=1;2;15;8
    Note over Productor: Valida y procesa la cadena de numeros
    Productor->>Exchange: RabbitTemplate.convertAndSend(Grupo1Routing, "1;2;15;8")
    Exchange->>Queue: Enruta el mensaje a Grupo1Queue
    Productor-->>Cliente: "Lista enviada a RabbitMQ correctamente."
    Queue->>Consumidor: Consumo asincrono via @RabbitListener
```

---

## Configuracion de RabbitMQ

Los parametros de configuracion implementados de acuerdo con la especificacion del examen son:

| Parametro | Definicion en el Examen | Valor Configurado |
|---|---|---|
| Cola (Queue) | NroGrupoQueue | Grupo1Queue |
| Intercambiador (Exchange) | NroGrupoExchange | Grupo1Exchange |
| Clave de Enrutamiento (Routing Key) | NroGrupoRouting | Grupo1Routing |
| Host del broker | spring.rabbitmq.host | localhost |
| Puerto AMQP | spring.rabbitmq.port | 5672 |
| Credenciales por defecto | spring.rabbitmq.username / password | guest / guest |

---

## Especificacion del Endpoint REST

### Envio de lista de posiciones

- **Metodo:** `GET`
- **Ruta:** `/api/fibonacci/send`
- **Parametro de consulta:** `numbers` (cadena de numeros enteros separados por punto y coma `;`)
- **Ejemplo de solicitud:**
  ```http
  GET /api/fibonacci/send?numbers=1;2;15;8 HTTP/1.1
  Host: localhost:8081
  ```
- **Respuesta satisfactoria (HTTP 200 OK):**
  ```text
  Lista enviada a RabbitMQ correctamente.
  ```

---

## Compilacion y Ejecucion

### 1. Iniciar RabbitMQ

Es necesario contar con una instancia activa de RabbitMQ en el puerto 5672. Se puede iniciar mediante Docker con el siguiente comando:

```bash
docker run -d --name rabbitmq-server -p 5672:5672 -p 15672:15672 rabbitmq:3-management
```

La consola web de administracion queda accesible en `http://localhost:15672` (usuario: `guest`, clave: `guest`).

### 2. Ejecutar el Servicio Productor

1. Clonar el repositorio:
```bash
git clone https://github.com/samuelchaupis-cloud/appGrupo1Productor.git
cd appGrupo1Productor
```

2. Compilar con Maven Wrapper:
- En entornos Unix (Linux / macOS):
```bash
./mvnw clean compile
```
- En entornos Windows:
```cmd
mvnw.cmd clean compile
```

3. Iniciar el servicio:
- En entornos Unix (Linux / macOS):
```bash
./mvnw spring-boot:run
```
- En entornos Windows:
```cmd
mvnw.cmd spring-boot:run
```

El servicio iniciara en el puerto `8081`.

---

## Ejemplo de Prueba

Para enviar las posiciones al bus de RabbitMQ mediante cURL:

```bash
curl -X GET "http://localhost:8081/api/fibonacci/send?numbers=1;2;15;8"
```

---

## Estructura de Ramas

El repositorio organiza el trabajo en las siguientes ramas:

- `main`: Rama principal con la version final y funcional del microservicio productor.
- `develop`: Rama de integracion para consolidar modificaciones antes del pase a `main`.
- Ramas por integrante:
  - `samuel`: Rama de trabajo de Jhonny Samuel Chaupis Alvarez.
  - `jhonny-chaupis`: Alias nominal para identificacion de integrante.
  - `ronald-cruz`: Rama de trabajo de Ronald Corwin Cruz Valdez.
  - `daniel-hinojosa`: Rama de trabajo de Carlos Daniel Hinojosa Cano.
  - `brayan-hurtado`: Rama de trabajo de Brayan Luis Hurtado Sernaque.
  - `mathias-alayo`: Rama de trabajo de Mathias Miller Alayo Oliveros.
  - `jmalayo`: Rama base del repositorio colegiado.
