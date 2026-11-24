# Forged API

API REST en Spring Boot + Kotlin + MySQL para la app Android **ProyectoApps_Forged**.

## Requisitos

- JDK 21 o superior (el proyecto compila con la toolchain 21).
- MySQL 8 en `localhost:3306`.
- La base de datos `forged_db` se crea automáticamente si no existe.

## Configuración

Archivo `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/forged_db?createDatabaseIfNotExist=true&useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC
spring.datasource.username=root
spring.datasource.password=corroncho820_1
server.port=8080
```

## Ejecutar

```bash
./gradlew bootRun
```

La API queda disponible en `http://localhost:8080` y escucha en todas las
interfaces (`0.0.0.0`) para que un celular en la misma red pueda conectarse.

## Estructura del proyecto

```text
src/main/kotlin/com/forged/forged_api/
├── ForgedApplication.kt     Punto de entrada de Spring Boot
├── config/                  Configuración web y CORS
├── controller/              Endpoints REST
├── dto/                     Objetos de petición y respuesta
├── entity/                  Entidades JPA
├── exception/               Manejo global de errores
├── repository/              Acceso a datos
├── security/                Hash de contraseñas
└── seed/                    Datos iniciales de ejemplo
```

## Probar la API

Con la API en ejecución, abre otra terminal y ejecuta:

```powershell
cd "C:\Users\MI PC\Documents\forged\forged"
powershell -ExecutionPolicy Bypass -File .\scripts\probar-api.ps1
```

El script comprueba login, registro duplicado, recuperación de contraseña,
clases, entrenadores, reservas, membresías, el aumento/liberación de cupos,
la disponibilidad por entrenador y día, las rutinas con sus ejercicios y el
registro/eliminación de pagos. Al final muestra
`RESULTADO: todas las pruebas pasaron.`

## Usuario de prueba

| Campo    | Valor            |
|----------|------------------|
| Correo   | `carlos@mail.com` |
| Password | `1234`           |

También existe la usuaria `ca@gmail.com` / `1234` (Camila), creada para probar
el registro y el flujo de la app desde el emulador.

Al iniciar, la API carga datos de ejemplo (usuario, entrenadores, clases,
una reserva, una membresía, rutinas con ejercicios, pagos y disponibilidad
horaria) si las tablas están vacías.

## Uso desde la app Android

La app `ProyectoApps_Forged` consume esta API como única fuente de datos de
negocio.

La conexión se hace por el túnel de adb (`http://127.0.0.1:8080/`). Antes de
usar la app ejecuta una vez (y cada vez que reinicies el emulador o el celular):

```bat
adb reverse tcp:8080 tcp:8080
```

En el proyecto Android está el archivo `scripts\conectar-api.bat`, que hace ese
paso automáticamente. Si el túnel no está activo, la app reintenta sola contra
`http://10.0.2.2:8080/` (el localhost del PC visto desde el emulador).

Si usas un celular físico conectado por USB, el túnel de `adb reverse` también
funciona y no necesitas cambiar ninguna IP.

Recursos usados por las pantallas:

- `PantallaReservar` y `PantallaMisClases`: `/clases` y `/reservas`.
- `PantallaDetalleEntrenador` y `PantallaDisponibilidad`: `/entrenadores` y
  `/disponibilidad?idEntrenador=&dia=`.
- `PantallaRutina` y `PantallaRutinaHoy`: `/rutinas` y `/ejercicios?idRutina=`.
- `PantallaPagos`, `PantallaPago` y `PantallaConfirmacionPago`: `/membresias`,
  `/pagos` y el `POST /pagos`.

## Endpoints

### Autenticación

| Método | Ruta                        | Descripción                          |
|--------|-----------------------------|--------------------------------------|
| POST   | `/auth/registro`            | Crea un usuario                      |
| POST   | `/auth/login`               | Valida correo y contraseña           |
| GET    | `/auth/usuario?email=`      | Recupera un usuario por correo       |
| GET    | `/auth/recuperar?email=`    | Indica si el correo está registrado  |

### Clases

| Método | Ruta              | Descripción             |
|--------|-------------------|-------------------------|
| GET    | `/clases`         | Lista de clases         |
| GET    | `/clases/{id}`    | Detalle de una clase    |
| POST   | `/clases`         | Crea una clase          |
| PUT    | `/clases/{id}`    | Actualiza una clase     |
| DELETE | `/clases/{id}`    | Elimina una clase       |

### Entrenadores

| Método | Ruta                     | Descripción              |
|--------|--------------------------|--------------------------|
| GET    | `/entrenadores`          | Lista de entrenadores    |
| GET    | `/entrenadores/primero`  | Primer entrenador        |
| GET    | `/entrenadores/{id}`     | Detalle de un entrenador |
| POST   | `/entrenadores`          | Crea un entrenador       |
| PUT    | `/entrenadores/{id}`     | Actualiza un entrenador  |
| DELETE | `/entrenadores/{id}`     | Elimina un entrenador    |

### Reservas

| Método | Ruta                          | Descripción                                  |
|--------|-------------------------------|----------------------------------------------|
| GET    | `/reservas`                   | Todas las reservas                           |
| GET    | `/reservas?email=`            | Reservas de un usuario                       |
| GET    | `/reservas/{id}`              | Detalle de una reserva                       |
| POST   | `/reservas`                   | Crea una reserva y ocupa un cupo             |
| DELETE | `/reservas/{id}`              | Cancela una reserva y libera el cupo         |

### Membresías

| Método | Ruta                          | Descripción                          |
|--------|-------------------------------|--------------------------------------|
| GET    | `/membresias`                 | Todas las membresías                 |
| GET    | `/membresias/actual?email=`   | Membresía más reciente del usuario   |
| POST   | `/membresias`                 | Registra una nueva membresía         |

### Rutinas

| Método | Ruta            | Descripción          |
|--------|-----------------|----------------------|
| GET    | `/rutinas`      | Lista de rutinas     |
| GET    | `/rutinas/{id}` | Detalle de una rutina |
| POST   | `/rutinas`      | Crea una rutina      |
| PUT    | `/rutinas/{id}` | Actualiza una rutina |
| DELETE | `/rutinas/{id}` | Elimina una rutina   |

### Ejercicios de rutina

| Método | Ruta                            | Descripción                         |
|--------|---------------------------------|-------------------------------------|
| GET    | `/ejercicios`                   | Todos los ejercicios                |
| GET    | `/ejercicios?idRutina=`         | Ejercicios de una rutina            |
| GET    | `/ejercicios/{id}`              | Detalle de un ejercicio             |
| POST   | `/ejercicios`                   | Crea un ejercicio                   |
| PUT    | `/ejercicios/{id}`              | Actualiza un ejercicio              |
| DELETE | `/ejercicios/{id}`              | Elimina un ejercicio                |

### Pagos

| Método | Ruta            | Descripción                |
|--------|-----------------|----------------------------|
| GET    | `/pagos`        | Lista de todos los pagos   |
| GET    | `/pagos?email=` | Pagos del usuario indicado |
| GET    | `/pagos/{id}`   | Detalle de un pago         |
| POST   | `/pagos`        | Registra un pago           |
| PUT    | `/pagos/{id}`   | Actualiza un pago          |
| DELETE | `/pagos/{id}`   | Elimina un pago            |

### Disponibilidad

| Método | Ruta                                      | Descripción                    |
|--------|-------------------------------------------|--------------------------------|
| GET    | `/disponibilidad`                         | Toda la disponibilidad         |
| GET    | `/disponibilidad?idEntrenador=&dia=`      | Filtra por entrenador y día    |
| GET    | `/disponibilidad/{id}`                    | Detalle de un horario          |
| POST   | `/disponibilidad`                         | Crea un horario                |
| PUT    | `/disponibilidad/{id}`                    | Actualiza un horario           |
| DELETE | `/disponibilidad/{id}`                    | Elimina un horario             |

## Errores

Los errores de negocio responden con `{"mensaje": "..."}` y el código HTTP
correspondiente (`400`, `401`, `404`, `409`). Ejemplos:

- `401` credenciales incorrectas.
- `409` correo ya registrado o clase sin cupos.
