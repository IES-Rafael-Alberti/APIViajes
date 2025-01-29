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

**5. Excepciones y códigos de estado**

En la aplicación uso aplicaciones personalizadas para asegurarme que la respuesta HTTP correcta siempre se suceda un error dentro de la API. En mi case devuelvo la siguiente información sobre la excepción:

Status: Código del error.
Error: Nombre del error.
Mensaje: Describiendo el problema, se escribe cuando se programa el salto de excepción.
Path: En que url de la API ha sucedido el error.

BadRequestException (Código 400 BAD_REQUEST)
Salta cuando los datos que ha introducido el usuario son incorrectos o nos siguen el formato correcto. Por ejemplo: Cuando el usuario ha dejado vacío un campo que es
obligatorio rellenar, una fecha no está escrita en el formato incorrecto o la contraseña no sigue las reglas necesarias.

NotFoundException (Código 404 NOT FOUND)
Salta cuando un recurso solicitado no existe o no puede ser encontrado. Suele ocurrir cuando se intenta leer, modificar o borrar un elemento con un id que no existe.

ConflictException (Código 409 CONFLICT)
Salta cuando algo que se está intentado hacer o un dato que se intenta introducir entra en conflicto con algo que ya existe en la base de datos. Por ejemplo: Intentar registrarse con un username que ya está en uso o intentar crear un destino con el mismo nombre de uno que ya está registrado. Lo mismo podría ocurrir al intentar actualizarlos.

excepción generica (Código 500 INTERNAL SERVER ERROR)
Si sucede una excepción inesperada, hay un tipo de excepción generica que controla la respuesta del servidor. En este caso una de las cosas que hace es que el mensaje
se vuelve génerico para que no devuelva todo el error, evitando que se filtren los fallos del código del servidor.

**6. Restricciones de seguridad**










   

