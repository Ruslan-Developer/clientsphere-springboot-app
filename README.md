# ClientSphere

**ClientSphere** es el proyecto de fin de curso del ciclo formativo **Desarrollo de Aplicaciones Multiplataforma**, correspondiente al curso 2024/2025, cursado en el centro **IES Ribera de Castilla**, Valladolid. 

**Tutor**: Cristina Silvan Pardo.

# API REST de Gestión de Usuarios, Clientes y Productos

Esta API REST ha sido desarrollada con **Spring Boot** para gestionar usuarios, clientes, facturas y productos en una aplicación. Permite realizar operaciones CRUD sobre los usuarios y clientes, así como gestionar productos y crear facturas asociadas a los usuarios.

## Características

- **Gestión de usuarios**: Creación, actualización, eliminación y consulta de usuarios.
- **Gestión de clientes**: Consultar todos los clientes, obtener clientes por apellido y crear nuevos clientes.
- **Gestión de productos**: Agregar, actualizar y consultar productos en el sistema.
- **Gestión de facturas**: Crear facturas asociadas a usuarios, con productos y precios.

## Tecnologías utilizadas

- **Spring Boot**: Framework principal para crear la API REST.
- **Spring Data JPA**: Para la gestión de la base de datos y las entidades (usuarios, clientes, productos, facturas).
- **Spring Security**: Para la autenticación y autorización de usuarios.
- **MySQL**: Base de datos para almacenar la información.
- **JWT**: Para la autenticación basada en tokens.

## Endpoints principales

1. **Usuarios**
   - `GET /api/users`: Listar todos los usuarios.
   - `POST /api/users`: Crear un nuevo usuario.
   - `PUT /api/users/{id}`: Actualizar un usuario existente.
   - `DELETE /api/users/{id}`: Eliminar un usuario.

2. **Clientes**
   - `GET /api/customers`: Listar todos los clientes.
   - `GET /api/customers/{lastname}`: Obtener clientes por su apellido.
   - `POST /api/customers`: Crear un nuevo cliente.

3. **Productos**
   - `GET /api/products`: Listar todos los productos.
   - `POST /api/products`: Crear un nuevo producto.
   - `PUT /api/products/{id}`: Actualizar un producto.
   - `DELETE /api/products/{id}`: Eliminar un producto.

## Instalación

1. Clona el repositorio:

    ```bash
    git clone https://github.com/Ruslan-Developer/spring_users_app_backend.git
    ```

2. Abre el proyecto en tu IDE favorito.

3. Configura las credenciales de la base de datos en `src/main/resources/application.properties` o `application.yml`.

4. Ejecuta el proyecto con Maven o Gradle:

    ```bash
    mvn spring-boot:run
    ```

5. La API estará disponible en `http://localhost:8080`.

## Autenticación

La API utiliza **JWT** para la autenticación. Al iniciar sesión, recibirás un token que debes incluir en el encabezado de las peticiones





## Agradecimientos

Gracias a **CRISTINA SILVAN PARDO** por su orientación y tutoría en este proyecto. Además, agradezco a JUAN ANTONIO ALONSO VELASCO por instruirme en este apartado y no menos al equipo del **IES Ribera de Castilla** por su apoyo en la formación.

