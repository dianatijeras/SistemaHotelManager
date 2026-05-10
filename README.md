#  HotelManager — Sistema de Gestión Hotelera

> Proyecto académico desarrollado para la asignatura **Ingeniería de Software I**  
> Universidad del Quindío · 2026

---

##  Descripción General

**HotelManager** es una aplicación web fullstack para la administración operativa de un hotel. Permite gestionar el ciclo completo de una estadía: desde la creación de la reserva hasta el check-out y el registro del pago.

El sistema contempla dos perfiles de usuario con vistas y permisos diferenciados: **Administrador** y **Recepcionista**. La persistencia de datos es en memoria (sin base de datos), lo que facilita la ejecución sin configuración adicional.

---

##  Tecnologías Utilizadas

### Backend
| Tecnología | Versión | Uso |
|---|---|---|
| Java | 17 | Lenguaje principal |
| Spring Boot | 3.2.0 | Framework backend |
| Spring Web | — | API REST |
| Spring Validation | — | Validación de DTOs |
| Maven | 3.x | Gestión de dependencias |

### Frontend
| Tecnología | Versión | Uso |
|---|---|---|
| React | 18.3.1 | Framework UI |
| TypeScript | — | Tipado estático |
| React Router | 7.13.0 | Enrutamiento SPA |
| Tailwind CSS | 4.x | Estilos |
| Vite | 6.3.5 | Bundler y servidor de desarrollo |
| pnpm | — | Gestor de paquetes |

---

##  Arquitectura del Proyecto

El proyecto sigue una arquitectura **cliente-servidor** con separación completa entre frontend y backend, comunicados mediante API REST con JSON.
```text
SistemaHotelManager/
│
├── backend/
│   └── backendHotel/
│       ├── src/
│       ├── pom.xml
│       └── target/
│
├── frontend/
│   └── frontendHotel/
│       ├── src/
│       ├── package.json
│       ├── vite.config.ts
│       └── node_modules/
│
└── package-lock.json
```

---
# Estructura del Backend

```text
backend/backendHotel/src/main/java/co/edu/uniquindio/sistemahotelmanager/
│
├── Controller/
├── data/
├── dto/
├── enums/
├── model/
├── service/
└── HotelApplication.java
```

## Componentes principales

### Controllers

Manejo de endpoints REST:

- AuthController
- DashboardController
- EstadiaController
- HabitacionController
- HuespedController
- PagoController
- ReservaController
- UsuarioController

### Models

Entidades principales del sistema:

- Usuario
- Huesped
- Administrador
- Recepcionista
- PersonalLimpieza
- Habitacion
- Reserva
- Estadia
- Pago
- CategoriaHabitacion

### Services

Lógica de negocio del sistema.

### DTOs

Objetos para intercambio de datos entre frontend y backend.

### DataInitializer

Clase encargada de cargar datos demo automáticamente al iniciar la aplicación.

---

# Estructura del Frontend

```text
frontend/frontendHotel/
│
├── src/
├── package.json
├── vite.config.ts
├── .env.example
├── SISTEMA_AUTH.md
└── index.html
```


---

##  Requisitos Previos

Antes de ejecutar el proyecto, asegúrate de tener instalado:

| Herramienta | Versión mínima | Verificar con |
|---|---|---|
| Java JDK | 17 | `java -version` |
| Apache Maven | 3.6+ | `mvn -version` |
| Node.js | 18+ | `node -version` |
| pnpm | 8+ | `pnpm -v` |

> **Instalar npm** (si no lo tienes):
> ```bash
> npm install
> ```

---

##  Ejecución del Backend

```bash
# 1. Abre el proyecto backend en tu IDE (IntelliJ IDEA, Eclipse o VS Code).
# 2. Navega hasta la clase principal en la siguiente ruta:
     src/main/java/co/edu/uniquindio/sistemahotelmanager/HotelApplication.java

# 3. Haz clic en el botón Run (▶) sobre la clase HotelApplication, o ejecútala con clic derecho → Run 'HotelApplication'.
```

Al arrancar correctamente, la consola mostrará:

```
   Datos iniciales cargados correctamente.
   Habitaciones : 8
   Huéspedes    : 3
   Recepcionistas: 2
```

> El backend queda disponible en `http://localhost:8080`  
> Los datos se cargan en memoria automáticamente. **No se requiere base de datos.**

---

##  Ejecución del Frontend

```bash
Abre una terminal y ejecuta los siguientes comandos en orden:
# 1. Entrar a la carpeta del frontend
cd frontend
cd frontendHotel

# 2. Instalar dependencias
npm install

# 3. Iniciar el servidor de desarrollo
npm run dev
```

> El frontend queda disponible en `http://localhost:5173`

---

##  URLs de Acceso

| Recurso | URL |
|---|---|
| Aplicación web | http://localhost:5173 |
| API REST (raíz) | http://localhost:8080/api |
| Login | http://localhost:5173/login |

> ⚠️ El backend **debe estar corriendo** antes de abrir el frontend.

---

##  Usuarios de Prueba (DEMO)

### Acceso al Frontend (login simulado)

| Rol | Usuario  | Contraseña  |
|---|----------|-------------|
| Administrador | `admin`  | `admin2024` |
| Recepcionista | `mari.l` | `admin123`  |

### Huéspedes precargados en el Backend

| ID | Nombre | Usuario | Contraseña |
|---|---|---|---|
| H-001 | Carlos Ramírez | `carlos.r` | `pass123` |
| H-002 | Laura Gómez | `laura.g` | `pass123` |
| H-003 | Andrés Torres | `andres.t` | `pass123` |

### Habitaciones precargadas

| Número | Tipo | Piso | Precio/noche | Capacidad |
|---|---|---|---|---|
| 101, 102, 103 | ESTANDAR | 1 | $150.000 | 2 |
| 201, 202 | SUPERIOR | 2 | $220.000 | 2 |
| 301, 302 | DELUXE | 3 | $350.000 | 3 |
| 401 | SUITE | 4 | $600.000 | 4 |

---

##  Endpoints de la API REST

### Reservas — `/api/reservas`
| Método | Endpoint | Descripción |
|---|---|---|
| `POST` | `/api/reservas` | Crear reserva |
| `GET` | `/api/reservas` | Listar todas |
| `GET` | `/api/reservas/{id}` | Buscar por ID |
| `GET` | `/api/reservas/huesped/{id}` | Reservas de un huésped |
| `PUT` | `/api/reservas/{id}/confirmar` | Confirmar |
| `PUT` | `/api/reservas/{id}/cancelar` | Cancelar |
| `PUT` | `/api/reservas/{id}/no-show` | Marcar No-Show |

### Habitaciones — `/api/habitaciones`
| Método | Endpoint | Descripción |
|---|---|---|
| `GET` | `/api/habitaciones` | Listar todas |
| `GET` | `/api/habitaciones/disponibles` | Solo disponibles |
| `GET` | `/api/habitaciones/limpieza` | Pendientes de limpieza |
| `PUT` | `/api/habitaciones/{numero}/limpieza` | Marcar en limpieza |
| `PUT` | `/api/habitaciones/{numero}/disponible` | Marcar disponible |
| `PUT` | `/api/habitaciones/{numero}/mantenimiento` | Marcar mantenimiento |
| `PUT` | `/api/habitaciones/{numero}/fuera-servicio` | Fuera de servicio |

### Estadías — `/api/estadias`
| Método | Endpoint | Descripción |
|---|---|---|
| `GET` | `/api/estadias` | Listar todas |
| `GET` | `/api/estadias/en-curso` | Solo activas |
| `GET` | `/api/estadias/{id}` | Buscar por ID |
| `POST` | `/api/estadias/checkin/{idReserva}` | Realizar check-in |
| `PUT` | `/api/estadias/{id}/checkout` | Realizar check-out |
| `POST` | `/api/estadias/consumo` | Agregar consumo |

### Pagos — `/api/pagos`
| Método | Endpoint | Descripción |
|---|---|---|
| `POST` | `/api/pagos` | Registrar pago |
| `GET` | `/api/pagos` | Listar todos |
| `GET` | `/api/pagos/estadia/{id}` | Pagos de una estadía |
| `PUT` | `/api/pagos/{id}/aprobar` | Aprobar |
| `PUT` | `/api/pagos/{id}/rechazar` | Rechazar |
| `PUT` | `/api/pagos/{id}/reembolsar` | Reembolsar |

### Huéspedes — `/api/huespedes`
| Método | Endpoint | Descripción |
|---|---|---|
| `POST` | `/api/huespedes` | Registrar huésped |
| `GET` | `/api/huespedes` | Listar todos |
| `GET` | `/api/huespedes/{id}` | Buscar por ID |
| `PUT` | `/api/huespedes/{id}` | Actualizar datos |

---

##  Funcionalidades Principales

### Por módulo

| Módulo | Funcionalidades |
|---|---|
| **Reservas** | Crear, listar, confirmar, cancelar, marcar No-Show |
| **Habitaciones** | Ver estado, cambiar a limpieza / disponible / mantenimiento |
| **Check-in** | Buscar reserva confirmada e iniciar estadía |
| **Check-out** | Buscar estadía activa, ver consumos y finalizar |
| **Estadías** | Ver detalle, agregar consumos (minibar, servicio, etc.) |
| **Pagos** | Registrar, aprobar, rechazar, reembolsar |
| **Huéspedes** | Registrar, listar, editar contacto |
| **Usuarios** | Crear y gestionar usuarios del sistema (solo ADMIN) |

### Validaciones de negocio implementadas

- Detección y bloqueo de **reservas duplicadas** por solapamiento de fechas
- Verificación de **capacidad** de habitación vs número de personas
- El **check-in** solo se permite si la reserva está RESERVADA o CONFIRMADA
- El **check-out** solo aplica sobre estadías EN_CURSO
- **Anti-doble cobro**: no se permite un segundo pago PAGADO sobre la misma estadía
- Al cancelar una reserva, la habitación vuelve automáticamente a DISPONIBLE
- Al hacer check-out, la habitación pasa automáticamente a EN_LIMPIEZA

---

##  Roles del Sistema

### ADMIN
- Acceso completo al sistema
- Gestión de usuarios (crear, activar/desactivar)
- Visualización de habitaciones y pagos
- Panel de estadísticas en el Dashboard

### RECEPCIONISTA
- Gestión de reservas (crear, confirmar, cancelar)
- Realizar check-in y check-out
- Ver y gestionar habitaciones
- Registrar y aprobar pagos
- Gestionar huéspedes


---

##  Flujo Operativo del Sistema

```
1. RESERVA
   └── Recepcionista crea una reserva
       └── Se selecciona huésped + habitación disponible + fechas
           └── Estado inicial: RESERVADA
               └── Se puede CONFIRMAR o CANCELAR

2. CHECK-IN
   └── El día de llegada, se busca la reserva (RESERVADA o CONFIRMADA)
       └── Se inicia la estadía
           └── Estado reserva → CHECKED_IN
           └── Estado habitación → OCUPADA

3. CONSUMOS (durante la estadía)
   └── Se pueden agregar consumos desde EstadiaDetalle
       └── Ejemplos: desayuno, minibar, servicio a la habitación
           └── Se recalcula el total automáticamente

4. CHECK-OUT
   └── Se busca la estadía activa (EN_CURSO)
       └── Se muestra resumen: habitación + consumos + total
           └── Se finaliza la estadía
               └── Estado estadía → FINALIZADA
               └── Estado habitación → EN_LIMPIEZA

5. PAGO
   └── Se registra el pago vinculado a la estadía
       └── Se selecciona monto y método (EFECTIVO, PSE, TARJETA, etc.)
           └── Estado inicial: PENDIENTE
               └── Se APRUEBA (confirma el cobro) o se RECHAZA
```

---

##  Solución de Problemas Frecuentes

**El frontend no puede conectarse al backend**
```
Error: No se puede conectar con el servidor
```
→ Verifica que el backend esté corriendo en `http://localhost:8080`  
→ Revisa que no haya otro proceso usando el puerto 8080: `netstat -an | grep 8080`

**Error CORS al llamar la API**  
→ La configuración CORS del backend permite `http://localhost:5173` por defecto.  
→ Si usas un puerto distinto, actualiza el origen permitido en `CorsConfig.java`.

**`pnpm` no se reconoce como comando**  
→ Instálalo con: `npm install -g pnpm`

**Maven no encuentra Java 17**  
→ Verifica con `java -version` y configura `JAVA_HOME` apuntando al JDK 17.

**Los datos se pierden al reiniciar**  
→ Es el comportamiento esperado. El sistema usa memoria RAM como persistencia.  
→ Los datos iniciales (habitaciones y huéspedes) se recargan automáticamente al reiniciar el backend.

---

##  Equipo de Desarrollo

> Proyecto desarrollado como trabajo académico para la asignatura  
> **Ingeniería de Software I — Universidad del Quindío**  
> **Juan Jose Suarez Moncaleano y Diana Maria Garcia Alzate**    
> Armenia, Quindío, Colombia · 2026

---

*Sistema desarrollado con fines académicos. No incluye autenticación real ni base de datos persistente.*


