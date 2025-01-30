# ADAT-3-Proyecto

## API usando SpringBoot

### 1. Idea
### Airplainee
Voy a hace una API en springboot. El objetivo de esta API es conectarse a una aplicación de móvil la cual permita a los usuarios organizarse en grupo a a la hora de realizar viajes,
pudiendo planear a donde quieren ir, en que fecha y de que forma se va a hacer el viaje, además de añadir quien va a participar.

He decido hacer esta API porque organizar un viaje puede ser un momento estresante y tener un lugar donde poner en común la planificación que sea accesible desde cualquier momento por
los participantes puede ayudar a aligerar el proceso y evitar quebraderos de cabeza en un futuro.

#### Tablas involucradas:
- **Tabla 1:** Usuarios
- **Tabla 2:** Viajes
- **Tabla 3:** Destinos

---
### 2. Tablas

#### Tabla Usuarios
| Campo        | Tipo   | Restricciones                                  |
|--------------|--------|------------------------------------------------|
| **id**       | Double | UNIQUE, PRIMARY KEY                            |
| **username** | String | 50 caracteres, UNIQUE                          |
| **password** | String |                                                |
| **roles**    | String | ENUM: ROL_USER, ROL_ADMIN                      |

**Notas:**
- El `id` autogenerado.
- La `password` tiene que tener al menos 8 caracteres, una letra y un número.
- La longitud máxima de `password` antes de ser hasheada es de 50 caracteres.

**Descripción:**
- Usuarios de la aplicación

---

#### Tabla Viajes
| Campo              | Tipo         | Restricciones                                    |
|--------------------|--------------|-------------------------------------------------|
| **id**             | Double       | UNIQUE, PRIMARY KEY                             |
| **destination**    | Destino      | SECONDARY KEY                                   |
| **date**           | Date         |                                                 |
| **method_of_travel** | String     | ENUM: car, bus, train, plane, boat             |
| **participants**   | Usuario      | SECONDARY KEY                                   |

**Notas:**
- El `id` autogenerado.
- La llave secundaria destination apunta solo a un destino
- La `date` debe ser el formato "yyyy-MM-dddd"
- Varios usuarios pueden estar en el mismo viaje

**Descripción:**
- Viajes planificados por los usuarios de la aplicación.

---

#### Tabla Destinos
| Campo      | Tipo   | Restricciones                                   |
|------------|--------|-------------------------------------------------|
| **id**     | String | UNIQUE, PRIMARY KEY, 10 caracteres              |
| **name**   | String | NOT NULL, UNIQUE, Máximo 50 caracteres          |
| **country**| String | NOT NULL                                        |

**Notas:**
- El `id` autogenerado.

### 3. Diagrama Clase-Entidad

![DiagramaRecuperacion](https://github.com/user-attachments/assets/d420cac4-21c1-463a-8f98-2fd8fe607d8f)

### 4. Endpoints

**Usuarios:**

- POST (/usuarios/login)
  Permite a los usuarios iniciar sesión
- POST (/usuarios/register)
  Permite a los usuarios registrarse y los añade a la base de datos
- GET (/usuarios)
  Devuelve todos los usuarios que estén en la base de datos
- GET (/usuarios/{id})
  Devuelve el usuario según su ID
- PUT (/usuarios/{id})
  Actualiza el usuario según su ID
- DELETE (/usuarios/{id})
  Borra a un usuario de la base de datos según su ID

**Viajes**
- POST (/viajes)
  Permite añadir viajes 
- GET (/viajes)
  Devuelve todas las reservas
- GET (/viajes/id)
  Devuelve el viaje segun el ID
- PUT (/viajes/id)
  Actualiza el viaje segun el ID
- PUT (/viajes/id)
  Cambia el metodo de viaje
- PUT (/viajes/id)
  Añade o elimina un participante del viaje
- DELETE (/viajes/id)
  Borra el viaje segun la ID

**Destinos**
- POST (/destinos)
  Añade un nuevo destino
- GET (/destinos)
  Devuelve todos los destinos
- GET (/destinos/id)
  Devuelve un destino por su ID
- PUT (/destinos/id)
  Actualiza el destino segun su ID
- DELETE (/destinos/id)
  Borra el destino segun su ID

**Viajes**
- POST (/viajes)
  Añade un nuevo viaje a la base de datos
- GET (/viajes)
  Devuelve todos los viajes almacenados en la base de datos
- GET (/viajes/id)
  Devuelve un viaje según su ID
- GET (/viajes/usuario/usuarioId)
  Devuelve todos los viajes donde cierto usuario sea participante, según el ID del usuario
- PUT (/viajes/id)
  Actualiza un viaje según su ID
- PUT (/viajes/id/join)
  Añade el usuario que esté logueado en el momento a la lista de participantes. El viaje objetivo
  coincide con el id
- PUT (/viajes/id/leave)
  Elimina el usuario que esté logueado en el momento de la lista de participantes. El viaje     objetivo coincide con el id
- DELETE (/viajes/id)
  Borra un viaje según su ID



### 5. Lógica de negocio

El objetivo de la aplicación es permitir a las personas organizar viajes en grupos para ello, he tenido en cuenta mantener la privacidad de los usuarios pero mantener abierta la posibilidad de que los administradores sean capaces de acceder en caso de que hay que solucionar algo.

**Los usuarios pueden:** Registrarse en la aplicación, inciar sesión y modificar o borrar su perfil como deseen. Además puede crear y unirse a viajes planeados, además de moficarlos.

**Los administradores deben/pueden:** Añadir o modificar los destinos presentes en la aplicación o borrarlos si fuese necesario. Aparte también tienen la habilidad de modifcar cualquier dato en general y acceder a la informacion completa para solucionar posibles incidencias.

- No hay forma de dar el rol de administrador a un usuario mediante la API, tiene que ser cambiado de forma manual. Al crear un usuario siempre se crea como rol usuario y al modificarse siempre se le asigna el rol que tenía antes asignado en la base de datos.

- Se asegura la privacidad de los usuarios limitando quien puede acceder perfiles o viajes. No se puede ver, modificar o borrar usuarios o viajes si no es el propio usuario o un miembro del viaje quien realiza la acción. La expección son los administradores que también pueden acceder.

- Los unicos que puede modificar de cualquier manera los destinos son los adminstradores, ya que los destinos son algo fijo que ofrece la propia aplicación, como ciudades o parques de atracciones.

- Los viajes pueden no tener participantes y por razones de seguridad al modificarse los viajes no se permite modificar a los participantes, se mantiene la lista que estuviese guardada anteriormente.

- Siguiendo el anterior punto, los únicos que pueden unirse o salirse de un viaje son los propios usuarios usando su sesión.

- Cuando se crea o modifica un viaje la fecha del viaje debe ser siempre una fecha futura.

- Tenemos un estandar mínimo en contraseñas, donde estas deben ser como mínimo 8 carácteres de largo, tener una letra y un número.

- Los nombres de usuario son únicos ya que queremos evitar confusión entre usuarios especialmente aquellos menos hábiles con las tecnologias.

- Los nombres de los destinos también son únicos para evitar confusión.

- Todo el mundo puede, incluso sin autenticar, ver todos los destinos registrados. Por el contrario los úncios que pueden ver todos los viajes o usuarios son los administradores.

### 6. Excepciones y códigos de estado

En la aplicación uso aplicaciones personalizadas para asegurarme que la respuesta HTTP correcta siempre se suceda un error dentro de la API. En mi case devuelvo la siguiente información sobre la excepción:

-Status: Código del error.
-Error: Nombre del error.
-Mensaje: Describiendo el problema, se escribe cuando se programa el salto de excepción.
-Path: En que url de la API ha sucedido el error.

-BadRequestException (Código 400 BAD_REQUEST)
Salta cuando los datos que ha introducido el usuario son incorrectos o nos siguen el formato correcto. Por ejemplo: Cuando el usuario ha dejado vacío un campo que es
obligatorio rellenar, una fecha no está escrita en el formato incorrecto o la contraseña no sigue las reglas necesarias.

-NotFoundException (Código 404 NOT FOUND)
Salta cuando un recurso solicitado no existe o no puede ser encontrado. Suele ocurrir cuando se intenta leer, modificar o borrar un elemento con un id que no existe.

-ConflictException (Código 409 CONFLICT)
Salta cuando algo que se está intentado hacer o un dato que se intenta introducir entra en conflicto con algo que ya existe en la base de datos. Por ejemplo: Intentar registrarse con un username que ya está en uso o intentar crear un destino con el mismo nombre de uno que ya está registrado. Lo mismo podría ocurrir al intentar actualizarlos.

-excepción generica (Código 500 INTERNAL SERVER ERROR)
Si sucede una excepción inesperada, hay un tipo de excepción generica que controla la respuesta del servidor. En este caso una de las cosas que hace es que el mensaje
se vuelve génerico para que no devuelva todo el error, evitando que se filtren los fallos del código del servidor.

### 7. Restricciones de seguridad

Endpoitns Públicos: Incluye aquellos para registrarse e iniciar sesión o ver los destinos disponibles
- POST /usuarios/login:
- POST /usuarios/register:
- GET /destinos:
- GET /destinos/id:

Endpoints para usuarios autorizados: Registrarse permite a los usuarios crear, unirse o dejar viajes
- POST /viajes
- PUT /viajes/id/join
- PUT /viajes/id/leave

Endpoints para adminsitradores: Los administradores tiene acceso a toda la información además de ser los unicos que pueden modificar los destinos disponibles
- GET /usuarios
- GET /viajes
- POST /destinos
- PUT /destinos/id
- DELETE /destinos/id

Endpoitns de usuario (pueden acceder los propios usuarios a ellos mismos o los administradores): Incluye las acciones sobre la cuenta de uno mismo
- GET /usuarios/id
- PUT /usuarios/id
- DELETE /usuarios/id

Enpoitns de participante (pueden acceder los participantes del viaje o los administradores): Incluye las acciones sobre las acciones sobre un viaje al que uno pertence.
- GET /viajes/id
- PUT /viajes/id
- GET /viajes/usuarios/usuarioId
- DELETE /viajes/id

Notas: Si algún endpoint no está incluido, por defecto se considera de accesso exclusivo para los administradores para evitar que se filtre cualquier tipo de información o función por error.

### 8. Teoría

## a. ¿Qué dependencias has utilzado?
He usado la página https://start.spring.io/ para crear y descargar el paquete de SpringBoot al cual le he añadido las siguientes dependencias:
-Spring Web
-Spring Data JPA
-OAuth2 Resource Server
-Spring Boot DevTools

Mi IDE durante el proyecto ha sido IntelliJ, XAMPP para lanzar un servidor Apache donde se encuentra la base de datos MySQL donde he usado phpMyAdmin como editor de base de datos. Para la pruebas de endpoints he usado Insomnia que me permite realizar pruebas rápidamente y de forma sencilla. Finalmente mi navegador predilecto ha sido Firefox.

## b. ¿Qué es una API REST? ¿Cuáles son los principios de una API REST? ¿Dónde identificas dichos principios dentro de tu implementación? 

Una API REST (Representational State Transfer) es un estilo de arquitectura de software para sistemas distribuidos, se usa sobre todo en aplicaciones web
Los principios fundamentales de una API REST son:

- Cliente-servidor: La unica forma de comunicar se entre el cliente y el servidor debe ser mediante solicitudes HTTP, deben estar asilados de cualquier otra forma.
- Sin estado (stateless): No se debe compartir información entre peticiones, es decir, sin estado. Todas las peticiones deben ser independientes y usar solo la información necesaria.
- Identificador único (URI): Todos los recursos deben mantener una jerarquía lógica y tener una URI única que no se repita.
- Uso correcto de HTTP: REST debe respetar tanto los verbos y códigos de estado para cada operación: GET, POST, PUT, DELETE, PATCH, etc...

En mi implementación los principios se representan de la siguiente forma:

- Cliente-servidor: La única forma de interactuar con el servidor es mediante los endpoints no hay otra forma de hacerlo.
- Sin estado: No se guarda nada en el servidor cuando se realiza una petición y se pide la información minima necesaria usando DTA que permitan recibir exactamente lo necesario.
- Identificador único: Los URIs se organizan por el tipo de dato al que afectan (usuario/viaje/destino) y ninguno se repite.
- Uso correcto de HTTP: Los endpoints GET son para pedir información, POST solo para crear nueva información (como register o login), PUT es para actualizar información ya existente en la base de datos y DELETE para elminarla. Además me aseguro que la repuesta HTTP siempre sea la correcta según la operación realizada incluse cuando ocurre un error, teniendo excepciones personalizadas que devuelven exactamente lo que ha pasado.

## c. ¿Qué ventajas tiene realizar una separación de responsabilidades entre cliente y servidor?

**Independencia y flexibilidad en el desarrollo:**

El equipo de frontend puede trabajar independientemente del equipo de backend. Se pueden realizar cambios en el cliente sin afectar al servidor y viceversa
Permite usar diferentes tecnologías en cada lado (por ejemplo, JetPack Compose en frontend y Springboot en backend)

**Escalabilidad mejorada:**

El servidor puede escalarse independientemente según la carga y se pueden agregar más servidores sin afectar a los clientes.
También permite optimizar recursos de manera específica para cada parte

**Mayor seguridad:**

El cliente no tiene acceso directo a la base de datos .Se puede implementar autenticación y autorización de manera centralizada. Los datos sensibles se pueden procesar y validar en el servidor

**Reutilización del backend:**

Un mismo servidor puede servir a múltiples clientes (web, móvil, desktop) y las APIs pueden ser consumidas por diferentes aplicaciones facilitando la integración con sistemas de terceros.

**Mantenimiento más sencillo:**

Los problemas se pueden aislar más fácilmente y las actualizaciones se pueden realizar por separado
La documentación puede mantenerse específica para cada parte.

**Mejor gestión de recursos:**

El procesamiento pesado se puede realizar en el servidor, por lo tanto se reduce la carga en los dispositivos cliente.











   

