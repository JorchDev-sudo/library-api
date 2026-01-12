📚 Library API

Library API es una API REST desarrollada con Spring Boot que gestiona una biblioteca digital
permitiendo administrar autores, libros, préstamos y usuarios
con autenticación segura basada en JWT y documentación automática mediante Swagger/OpenAPI.

🚀 Características principales:

  -🔐 Autenticación y autorización con JWT (stateless)

  -🧑‍💻 Gestión completa de:
    Autores
    Libros
    Usuarios
    Préstamos

  -📄 Paginación y ordenamiento

  -🧾 DTOs + MapStruct para desacoplar dominio y API

  -🧠 Manejo global de excepciones

  -📑 Documentación interactiva con Swagger

  -🧪 Tests unitarios y de seguridad

  -🛠 Migraciones de base de datos con Flyway

  -🌍 Configuración por perfiles (dev / prod)

Arquitectura en capas claramente separadas:
client
 ├── controllers
 └── security

server
 ├── services
 ├── dto
 ├── mappers
 ├── exceptions
 └── handlers

persistence
 ├── entities
 └── repositories

 🛠️ Stack tecnológico:

Java 17

Spring Boot 3.2.5

Spring Web

Spring Data JPA

Spring Security (JWT)

MapStruct

Flyway

H2 (dev)

PostgreSQL (prod)

Swagger / OpenAPI (springdoc)

JUnit 5 + Mockito

Maven

🔐 Seguridad

Autenticación basada en JWT

Filtros personalizados (JwtAuthenticationFilter)

Implementación propia de UserDetailsService

Protección por roles y endpoints

Configuración completamente stateless

⚙️ Configuración y ejecución
1️⃣ Clonar el repositorio
git clone https://github.com/JorchDev-sudo/library-api.git
cd library-api

2️⃣ Variables de entorno requeridas
JWT_SECRET

3️⃣ Ejecutar en entorno de desarrollo
mvn spring-boot:run

Por defecto:

Base de datos: H2

Perfil activo: dev

🌍 Perfiles
Perfil	Base de datos
dev	H2 
prod	PostgreSQL y Flyway

La activación se realiza mediante:
spring.profiles.active=dev
