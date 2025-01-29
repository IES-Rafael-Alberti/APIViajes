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
  Permite a los usuarios registrarse

**Viajes**
- POST (/viajes)
  Permite añadir viajes 
- GET (/viajes)
  Devuelve todas las reservas
- GET (/viajes/id)
  Devuelve el viaje segun el id
- PUT (/viajes/id)
  Actualiza el viaje segun el id
- PUT (/viajes/id)
  Cambia el metodo de viaje
- PUT (/viajes/id)
  Añade o elimina un participante del viaje
- DELETE (/viajes/id)
  Borra el viaje segun la id

**Destinos**
- POST (/destinos)
  Añade un nuevo destino
- GET (/destinos)
  Devuelve todos los destinos
- GET (/destinos/id)
  Devuelve un destino por su id
- GET (/destinos)
  Devuelve todos los destinos que esten en un pais concreto
- PUT (/destinos/id)
  Actualiza el destino segun su id


### 5. Lógica de negocio

Puede haber hasta 50 personas registradas en el mismo viaje

Los usuarios solo pueden acceder al los viajes en los que estén registrados
-Si se intenta entrar si derechos de admin la respuesta es: 403 Forbidden
Los administradores pueden acceder a todos los viajes y son los únicos que pueden modificar los destinos
-Si se intenta entrar si derechos de admin la respuesta es: 403 Forbidden
No se pueden modificar las fechas de viaje a días anteriores a hoy
-Si se intenta devuelve: 416 Requested Range Not Satisfiable






   

