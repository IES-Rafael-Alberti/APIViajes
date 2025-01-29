# ADAT-3-Proyecto

## API usando SpringBoot

### 1. Idea

Voy a hacer una API REST de gestión de viajes en grupo (uno de los ejemplos). El objetivo es que la API permita a grupos de personas sincronizarse para hacer viajes.

**Idea:** Aplicación de gestión de viajes en grupo

#### Tablas involucradas:
- **Tabla 1:** Usuarios
- **Tabla 2:** Viajes
- **Tabla 3:** Destinos

---

### 2. Tablas

#### Tabla Usuarios
| Campo        | Tipo   | Restricciones                                  |
|--------------|--------|------------------------------------------------|
| **id**       | String | UNIQUE, PRIMARY KEY, 10 caracteres             |
| **username** | String | 30 caracteres                                  |
| **password** | String |                                                |
| **roles**    | String | ENUM: ROL_USER, ROL_ADMIN                      |
| **dni**      | String | NOT NULL, 9 caracteres                         |

**Notas:**
- El `id` autogenerado.
- El `dni` tiene que seguir las restricciones que aseguran que sea correcto (que las letras sean las correctas segun los numeros).

**Descripción:**
- Usuarios de la aplicación

---

#### Tabla Viajes
| Campo              | Tipo         | Restricciones                                    |
|--------------------|--------------|-------------------------------------------------|
| **id**             | String       | UNIQUE, PRIMARY KEY, 10 caracteres             |
| **destination**    | Destino      | SECONDARY KEY                                   |
| **date**           | Date         |                                                 |
| **method_of_travel** | String     | ENUM: car, bus, train, plane, boat             |
| **participants**   | Usuario      | SECONDARY KEY                                   |

**Notas:**
- El `id` autogenerado.
- La llave secundaria destination apunta solo a un destino
- Varios usuarios pueden estar en el mismo viaje

---

#### Tabla Destinos
| Campo      | Tipo   | Restricciones                                   |
|------------|--------|------------------------------------------------|
| **id**     | String | UNIQUE, PRIMARY KEY, 10 caracteres            |
| **name**   | String | NOT NULL, Máximo 20 caracteres                 |
| **country**| String | NOT NULL, Máximo 20 caracteres                 |

**Notas:**
- El `id` autogenerado.

### 3. Diagrama Clase-Entidad

  ![Diagrama ClaseEntidad drawio](https://github.com/user-attachments/assets/1ab8161d-8bd8-4c0c-b30c-0b3065812e36)

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






   

