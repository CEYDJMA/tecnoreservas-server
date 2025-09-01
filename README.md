# Sistema de Gestión de Citas e Inventario

Este repositorio contiene el código fuente de un sistema de **Gestión de Citas e Inventario**. Es una aplicación backend desarrollada en **Java** con el framework **Spring Boot**, diseñada para administrar recursos, personal y la programación de citas.

## Tabla de Contenido

- [Tecnologías](#tecnologías)
- [Dependencias Clave](#dependencias-clave)
- [Cómo Empezar](#cómo-empezar)
  - [Prerrequisitos](#prerrequisitos)
  - [Instalación](#instalación)
- [Ejecutar la Aplicación](#ejecutar-la-aplicación)
  - [Configuración](#configuración)
  - [Arranque](#arranque)
- [Documentación de la API](#documentación-de-la-api)
- [Licencia](#licencia)

## Tecnologías

La aplicación está construida sobre un stack de tecnologías moderno y robusto:

- **Java 21**: Versión del lenguaje de programación.
- **Spring Boot 3.5.0**: Framework principal para el desarrollo de la aplicación.
- **Spring Data JPA**: Para la persistencia de datos y comunicación con la base de datos.
- **Spring Security**: Para la gestión de autenticación y autorización.
- **PostgreSQL**: Sistema de gestión de bases de datos relacional.
- **Maven**: Herramienta para la gestión de dependencias y construcción del proyecto.

## Dependencias Clave

El proyecto utiliza las siguientes librerías para funcionalidades críticas:

| Dependencia                               | Propósito                                            |
| ----------------------------------------- | ---------------------------------------------------- |
| `flyway-core` & `flyway-database-postgresql` | Gestión de migraciones de la base de datos.          |
| `java-jwt`                                | Creación y validación de JSON Web Tokens (JWT).      |
| `springdoc-openapi-starter-webmvc-ui`     | Generación de documentación de la API con Swagger.   |
| `mapstruct`                               | Mapeo de objetos (ej. entre Entidades y DTOs).       |
| `lombok`                                  | Reducción de código repetitivo en clases Java.       |
| `spring-dotenv`                           | Carga de variables de entorno desde un archivo `.env`. |

## Cómo Empezar

Sigue estos pasos para configurar el entorno de desarrollo local.

### Prerrequisitos

- **JDK 21**: Java Development Kit.
- **Maven 3.x**: Gestor de dependencias.
- **PostgreSQL**: Base de datos.

### Instalación

1.  **Clona el repositorio:**
    ```bash
    git clone <URL_DEL_REPOSITORIO>
    cd proyecto-tecno
    ```
2.  **Instala las dependencias:**
    ```bash
    mvn clean install
    ```

## Ejecutar la Aplicación

### Configuración

La aplicación utiliza la dependencia `spring-dotenv` para gestionar la configuración sensible, como las credenciales de la base de datos.

1.  Crea un archivo llamado `.env` en la raíz del proyecto.
2.  Añade las siguientes variables con tus valores locales:

    ```dotenv
    # URL de conexión a tu base de datos PostgreSQL
    DB_URL=jdbc:postgresql://localhost:5432/tu_base_de_datos

    # Usuario y contraseña de la base de datos
    DB_USER=tu_usuario
    DB_PASSWORD=tu_contraseña

    # Configuración de JWT (puedes cambiar estos valores)
    JWT_SECRET_KEY=tu_clave_secreta_muy_larga_y_segura
    JWT_ISSUER=gestion-citas-api
    JWT_EXPIRATION_MS=86400000 # 24 horas
    ```

### Arranque

Una vez configurado el archivo `.env`, puedes iniciar la aplicación con el siguiente comando de Maven:

```bash
mvn spring-boot:run
```

Flyway aplicará automáticamente las migraciones de la base de datos al arrancar.

## Documentación de la API

Una vez que la aplicación esté en ejecución, puedes acceder a la documentación interactiva de la API (generada por Swagger) en tu navegador:

[http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

Desde esta interfaz podrás explorar todos los endpoints, ver los modelos de datos y probar la API directamente.

## Licencia

Este proyecto está bajo la Licencia Apache 2.0.