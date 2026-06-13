<p align="center">
  <img src="https://img.shields.io/badge/Java-17-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white"/>
  <img src="https://img.shields.io/badge/Spring_Boot-3.5-6DB33F?style=for-the-badge&logo=springboot&logoColor=white"/>
  <img src="https://img.shields.io/badge/Spring_Modulith-1.4-6DB33F?style=for-the-badge&logo=spring&logoColor=white"/>
  <img src="https://img.shields.io/badge/PostgreSQL-15-4169E1?style=for-the-badge&logo=postgresql&logoColor=white"/>
  <img src="https://img.shields.io/badge/Docker-ready-2496ED?style=for-the-badge&logo=docker&logoColor=white"/>
</p>

<h1 align="center">🎵 Kreitefy API</h1>

<p align="center">
  Backend REST de una plataforma de streaming musical construido como un <strong>monolito modular</strong> con <strong>arquitectura hexagonal (Ports & Adapters)</strong> y principios de <strong>Domain-Driven Design (DDD)</strong>.<br/>
  Diseñado para demostrar patrones de arquitectura de software de nivel profesional en un entorno académico.
</p>

---

## 📑 Tabla de contenidos

1. [Descripción general](#descripción-general)
2. [Arquitectura: DDD + Hexagonal](#arquitectura-ddd--hexagonal)
3. [Monolito Modular con Spring Modulith](#monolito-modular-con-spring-modulith)
4. [Comunicación entre módulos](#comunicación-entre-módulos)
5. [Dependencias clave](#dependencias-clave)
6. [Testing: JUnit 5, Mockito y ArchUnit](#testing-junit-5-mockito-y-archunit)
7. [Seguridad: JWT + OAuth2](#seguridad-jwt--oauth2)
8. [Perfiles y configuración](#perfiles-y-configuración)
9. [Puesta en marcha](#puesta-en-marcha)
10. [Docker](#docker)
11. [Estructura del proyecto](#estructura-del-proyecto)

---

## Descripción general

**Kreitefy** es una API REST para una plataforma musical que gestiona:

| Módulo       | Responsabilidad                                         |
|--------------|---------------------------------------------------------|
| `product`    | Catálogo de **canciones**, **álbumes**, **artistas** y **estilos musicales** |
| `community`  | **Canciones favoritas** por usuario                      |
| `users`      | **Autenticación** (JWT + OAuth2 Google/GitHub) y gestión de usuarios |
| `shared`     | Contratos genéricos, excepciones de dominio y utilidades transversales |

---

## Arquitectura: DDD + Hexagonal

El proyecto implementa simultáneamente **Domain-Driven Design** y la **Arquitectura Hexagonal** (también conocida como Ports & Adapters o Clean Architecture).

### ¿Qué es la Arquitectura Hexagonal?

La idea central es que el **dominio de negocio** es el núcleo de la aplicación y no debe depender de ningún detalle técnico externo (base de datos, HTTP, frameworks). El resto del mundo se comunica con ese núcleo a través de **puertos** (interfaces) y **adaptadores** (implementaciones).

```
┌──────────────────────────────────────────────────────────────┐
│                     INFRAESTRUCTURA                          │
│  ┌──────────────────────────────────────────────────────┐    │
│  │                    APLICACIÓN                        │    │
│  │  ┌────────────────────────────────────────────────┐  │    │
│  │  │               DOMINIO                          │  │    │
│  │  │  • Entidades de dominio (User, Song, Album…)   │  │    │
│  │  │  • Value Objects                               │  │    │
│  │  │  • Excepciones de dominio                      │  │    │
│  │  └────────────────────────────────────────────────┘  │    │
│  │  • Puertos de entrada  (Use Cases / interfaces)      │    │
│  │  • Puertos de salida   (RepositoryPort / interfaces) │    │
│  │  • Servicios de aplicación (orquestadores)           │    │
│  └─────────────────────────────────────────────────────-┘    │
│  • Adaptadores REST    → RestController + DTOs               │
│  • Adaptadores JPA     → RepositoryAdapter + JpaRepository   │
│  • Adaptadores Auth    → JwtService, OAuth2 handlers         │
└──────────────────────────────────────────────────────────────┘
```

### Regla de dependencia

> **El dominio nunca importa nada de la capa de aplicación ni de infraestructura. La aplicación nunca importa nada de infraestructura.**

Esto se verifica automáticamente en cada build mediante **ArchUnit** (ver sección de testing).

### Ejemplo: flujo de una petición

```
HTTP Request
    │
    ▼
RestController          [infrastructure.rest]
    │  (llama al puerto de entrada)
    ▼
UseCase interface       [application.ports.in]
    │  (implementado por)
    ▼
ApplicationService      [application.services]
    │  (llama al puerto de salida)
    ▼
RepositoryPort interface [application.ports.out]
    │  (implementado por)
    ▼
RepositoryAdapter       [infrastructure.persistence.adapter]
    │  (delega en)
    ▼
JpaRepository           [infrastructure.persistence.jpa]
    │
    ▼
Base de datos (PostgreSQL / H2)
```

### MapStruct — Mapeo entre capas

Cada capa maneja su propio modelo:

| Capa           | Modelo                     |
|----------------|---------------------------|
| Dominio        | Records de Java (`User`, `Song`…) |
| Infraestructura (JPA) | Entidades JPA (`UserEntity`, `SongEntity`…) |
| Infraestructura (REST) | DTOs de request/response  |

Las conversiones entre modelos las realiza **MapStruct** (`org.mapstruct:mapstruct:1.5.5.Final`), que genera el código de mapeo en tiempo de compilación (anotación `@Mapper`). Esto elimina código boilerplate y proporciona conversiones de tipo seguro, verificadas en compile-time.

```java
// Ejemplo de mapper generado por MapStruct
@Mapper(componentModel = "spring")
public interface UserMapper extends EntityMapper<User, UserEntity> {
    User entityToDomain(UserEntity entity);
    UserEntity domainToEntity(User user);
    List<User> toDomainListFromEntity(List<UserEntity> entities);
}
```

---

## Monolito Modular con Spring Modulith

### ¿Qué problema resuelve?

Un monolito tradicional tiende a convertirse en un **Big Ball of Mud**: todos los paquetes se importan entre sí libremente y es imposible refactorizar sin romper todo. Los microservicios resuelven el acoplamiento, pero añaden enorme complejidad operativa.

**Spring Modulith** ofrece una tercera vía: un único proceso desplegable (monolito) con **fronteras de módulo estrictamente reforzadas en tiempo de compilación y de test**. Esto permite:

- **Independencia de módulos**: cada módulo es responsable de su propio dominio.
- **Evolución controlada**: las dependencias entre módulos son explícitas y auditables.
- **Camino a microservicios**: si la escala lo requiere, cada módulo puede extraerse sin reescritura.

### Módulos del proyecto

```
com.kreitefy.api
├── product/        ← Módulo de catálogo musical
├── community/      ← Módulo de interacción de usuarios
├── users/          ← Módulo de autenticación y usuarios
└── shared/         ← Módulo compartido (NamedInterface)
```

Cada directorio de primer nivel bajo `com.kreitefy.api` es reconocido automáticamente por Spring Modulith como un **módulo independiente**.

### `package-info.java` — Control de visibilidad

Spring Modulith utiliza anotaciones en `package-info.java` para marcar qué tipos son públicos fuera del módulo:

```java
// users/domain/models/package-info.java
@org.springframework.modulith.ApplicationModule(
    allowedDependencies = "shared"
)
package com.kreitefy.api.users.domain.models;
```

Solo los tipos marcados con `@NamedInterface` o en paquetes explícitamente exportados pueden ser referenciados por otros módulos.

### Verificación en tiempo de test

```java
// ApiApplicationTests.java
@Test
void verificarModulos() {
    ApplicationModules.of(ApiApplication.class)
            .verify();          // ← falla el build si hay ciclos o violaciones
}
```

Si un módulo importa un tipo interno de otro módulo (no exportado), este test falla automáticamente en el pipeline de CI/CD.

---

## Comunicación entre módulos

Los módulos se comunican exclusivamente a través de **interfaces públicas** (`@NamedInterface`) y nunca a través de paquetes internos. Hay dos mecanismos:

### 1. Llamada directa a través de NamedInterface

El módulo `shared` expone la interfaz `CrudService` marcada con `@NamedInterface`:

```java
@org.springframework.modulith.NamedInterface
public interface CrudService<T, ID>
        extends GetAllUseCase<T>, PostUseCase<T>, GetFindUseCase<T,ID>,
                UpdateUseCase<T>, DeleteUseCase<ID> { }
```

Los módulos `product`, `community` y `users` extienden esta interfaz para sus propios servicios, reutilizando el contrato sin copiar código.

### 2. Eventos de dominio (Spring Application Events)

Para desacoplar completamente los módulos que no deben conocerse entre sí, Spring Modulith recomienda el patrón de **Domain Events** con `ApplicationEventPublisher`. Esta arquitectura está preparada para incorporar este patrón a medida que crezca la complejidad de las interacciones entre módulos.

```
┌──────────┐    evento    ┌────────────┐
│  product │ ──────────→  │ community  │
│  (emite) │              │ (escucha)  │
└──────────┘              └────────────┘
```

---

## Dependencias clave

| Dependencia | Versión | Rol |
|---|---|---|
| `spring-boot-starter-parent` | **3.5.14** | Base del proyecto |
| `spring-modulith-starter-core` | **1.4.11** | Módulos verificados en runtime/test |
| `spring-modulith-starter-jpa` | **1.4.11** | Integración Modulith + JPA |
| `spring-modulith-starter-test` | **1.4.11** | Utilidades de test por módulo |
| `mapstruct` | **1.5.5.Final** | Mapeo entre modelos en compile-time |
| `querydsl-core` + `querydsl-jpa` | **5.1.0** | Consultas tipadas y dinámicas |
| `archunit-junit5` | **1.3.0** | Reglas de arquitectura como tests |
| `jjwt-api/impl/jackson` | **0.12.6** | Creación y validación de JWT |
| `spring-boot-starter-security` | (BOM) | Spring Security 6 |
| `spring-boot-starter-oauth2-client` | (BOM) | OAuth2 con Google y GitHub |
| `spring-boot-starter-validation` | (BOM) | Validación de beans con Jakarta |
| `spring-boot-starter-data-jpa` | (BOM) | Spring Data JPA + Hibernate |
| `postgresql` | (BOM) | Driver PostgreSQL (prod) |
| `h2` | (BOM) | Base de datos en memoria (dev) |

---

## Testing: JUnit 5, Mockito y ArchUnit

El proyecto implementa una **pirámide de testing** completa con tres niveles de pruebas:

### 🔬 Nivel 1: Tests unitarios con Mockito

Prueban la lógica de negocio de un servicio de forma completamente aislada. Todas las dependencias son **mocks** generados por Mockito, sin levantar el contexto de Spring.

```java
@ExtendWith(MockitoExtension.class)
class FavoriteSongsServiceImplTest {

    @Mock
    private FavoriteSongRepositoryPort favoriteSongRepositoryPort;

    @InjectMocks
    private FavoriteSongsServiceImpl favoriteSongsService;

    @Test
    @DisplayName("Should not duplicate saving a song to favorites if it is already a favorite")
    void shouldNotDuplicateFavoriteWhenAlreadyExists() {
        when(favoriteSongRepositoryPort.exists("jose", 42L)).thenReturn(true);

        FavoriteSong result = favoriteSongsService.addFavorite("jose", 42L);

        verify(favoriteSongRepositoryPort, never()).save(any(FavoriteSong.class)); // ← regla de negocio
    }
}
```

**Cobertura de tests unitarios:**
- `FavoriteSongsServiceImplTest` — lógica de favoritos (añadir, no duplicar, eliminar, paginar)
- `UsuarioRepositoryImplTest` — adaptador de persistencia de usuarios + **Optimistic Locking**

### 🔗 Nivel 2: Tests de integración

Levantan un contexto de Spring parcial (slice de test) con una base de datos H2 en memoria para verificar que los adaptadores JPA funcionan correctamente con la base de datos real.

```
AlbumRepositoryPortImplIntegrationTest
ArtistaRepositoryPortImplIntegrationTest
CancionRepositoryPortImplIntegrationTest
UsuarioRepositoryImplIntegrationTest
GlobalExceptionHandlerIntegrationTest
```

### 🌐 Nivel 3: Tests End-to-End

Prueban el flujo HTTP completo de autenticación contra la API con Spring Security activo:

```
AuthRestControllerE2ETest  ← login, refresh token, acceso a rutas protegidas
```

### 🏛️ ArchUnit — Reglas de arquitectura como código

ArchUnit convierte las reglas de arquitectura en **tests que fallan el build si se violan**. Garantizan que la arquitectura hexagonal nunca se degrade:

```java
@AnalyzeClasses(packages = "com.kreitefy.api", importOptions = ImportOption.DoNotIncludeTests.class)
public class ArchitectureTest {

    // REGLA 1: El dominio NUNCA depende de aplicación ni infraestructura
    @ArchTest
    public static final ArchRule domain_should_not_depend_on_application_or_infrastructure =
            noClasses().that().resideInAPackage("..domain..")
                    .should().dependOnClassesThat()
                    .resideInAnyPackage("..application..", "..infrastructure..");

    // REGLA 2: La aplicación NUNCA depende de infraestructura
    @ArchTest
    public static final ArchRule application_should_not_depend_on_infrastructure = ...

    // REGLA 3: Los @RestController solo existen en infrastructure.rest
    @ArchTest
    public static final ArchRule controllers_should_only_be_in_infrastructure_rest = ...

    // REGLA 4: Los @Repository solo existen en infrastructure.persistence
    @ArchTest
    public static final ArchRule repositories_should_only_be_in_infrastructure_persistence = ...

    // REGLA 5: Los @RestController no acceden a JpaRepository directamente
    @ArchTest
    public static final ArchRule controllers_should_not_access_jpa_repositories_directly = ...

    // REGLA 6: Los @RestController no acceden a entidades JPA directamente
    @ArchTest
    public static final ArchRule controllers_should_not_access_jpa_entities_directly = ...

    // REGLA 7: Las entidades @Entity solo existen en infrastructure.persistence.entity
    @ArchTest
    public static final ArchRule jpa_entities_should_only_be_in_persistence_entity = ...
}
```

Con estas 7 reglas, **ningún desarrollador puede romper accidentalmente la arquitectura** sin que el build falle.

---

## Seguridad: JWT + OAuth2

La seguridad está implementada en el módulo `users` con una cadena de filtros de Spring Security 6:

```
Request → JwtAuthenticationFilter → SecurityFilterChain → Controller
```

### Autenticación JWT

- **Librería**: `io.jsonwebtoken` (JJWT 0.12.6), la implementación más moderna de JWT para Java.
- **Flujo**: el cliente envía credenciales → `AuthService` las valida → `JwtService` genera un `accessToken` + `refreshToken`.
- **Stateless**: `SessionCreationPolicy.STATELESS` — el servidor no guarda estado de sesión.
- **`@Version` (Optimistic Locking)**: los recursos de usuario incluyen un campo `version` que protege contra actualizaciones concurrentes. Si dos usuarios editan el mismo recurso simultáneamente, el segundo recibirá un error 409 Conflict.

### OAuth2 Social Login

Integración nativa con **Google** y **GitHub** mediante `spring-boot-starter-oauth2-client`:

```
Usuario → /oauth2/authorization/google → Google → callback → JwtTokenGenerado → Frontend
```

El `CustomOAuth2UserService` mapea el perfil de OAuth2 al modelo de dominio `User`, y el `OAuth2AuthenticationSuccessHandler` genera el JWT y redirige al frontend con el token.

### Roles y autorización

Los endpoints se protegen con `@PreAuthorize` usando roles definidos en el enum `RolType` (`USUARIO`, `ADMIN`), con `@EnableMethodSecurity` habilitado.

---

## Perfiles y configuración

| Perfil | Base de datos | Uso |
|---|---|---|
| `dev` (por defecto) | H2 en fichero (`./data/kreitefy`) | Desarrollo local sin infraestructura |
| `prod` | PostgreSQL 15 | Producción / Docker |
| `test` | H2 en memoria | Tests de integración |

Las variables sensibles se externalizan con el patrón `${VAR:default}`:

```properties
app.jwt.secret=${JWT_SECRET:dev-secret-fake}
spring.datasource.password=${DB_PASSWORD:}
```

---

## Puesta en marcha

### Requisitos

- **Java 17** (Temurin / Eclipse OpenJDK recomendado)
- **Maven 3.9+** (o usar el wrapper `./mvnw`)
- **Docker + Docker Compose** (solo para producción)

### Modo desarrollo (H2, sin base de datos externa)

```bash
# Clonar el repositorio
git clone https://github.com/tu-usuario/kreitefy-api.git
cd kreitefy-api

# Arrancar con el perfil dev (activo por defecto)
./mvnw spring-boot:run

# La API estará en: http://localhost:8080/api
# Consola H2:       http://localhost:8080/api/h2-console
```

### Ejecutar los tests

```bash
# Todos los tests (unitarios + integración + arquitectura + modulith)
./mvnw test

# Solo la clase de arquitectura
./mvnw test -Dtest=ArchitectureTest

# Solo la verificación de módulos
./mvnw test -Dtest=ApiApplicationTests#verificarModulos
```

---

## Docker

### Variables de entorno requeridas en producción

| Variable | Descripción |
|---|---|
| `JWT_SECRET` | Secreto para firmar los tokens JWT (mínimo 256 bits) |
| `SPRING_DATASOURCE_URL` | URL JDBC de PostgreSQL |
| `SPRING_DATASOURCE_USERNAME` | Usuario de la base de datos |
| `SPRING_DATASOURCE_PASSWORD` | Contraseña de la base de datos |
| `GOOGLE_CLIENT_ID` | Client ID de la app OAuth2 en Google Console |
| `GOOGLE_CLIENT_SECRET` | Client Secret de Google |
| `GITHUB_CLIENT_ID` | Client ID de la app OAuth2 en GitHub |
| `GITHUB_CLIENT_SECRET` | Client Secret de GitHub |

### Construir y ejecutar con Docker

```bash
# Construir la imagen (multi-stage, ~200 MB resultado final)
docker build -t kreitefy-api:latest .

# Ejecutar contra una PostgreSQL ya disponible
docker run -d \
  -p 8080:8080 \
  -e SPRING_PROFILES_ACTIVE=prod \
  -e JWT_SECRET=my-super-secret-key-256-bits \
  -e SPRING_DATASOURCE_URL=jdbc:postgresql://host.docker.internal:5432/kreitefy \
  -e SPRING_DATASOURCE_USERNAME=kreitefy \
  -e SPRING_DATASOURCE_PASSWORD=secret \
  --name kreitefy-api \
  kreitefy-api:latest
```

### Docker Compose (stack completo)

El proyecto cuenta con una configuración de Docker Compose que levanta de forma automática el servidor PostgreSQL, Adminer (gestor de base de datos) y la API Spring Boot.

Puedes ver la configuración detallada en [docker-compose.yml](./docker-compose.yml).

Para iniciar todo el entorno en segundo plano:

```bash
# Arrancar el stack completo
docker compose up -d

# Ver logs de la API
docker compose logs -f api
```

### 📊 Carga de datos de prueba

El proyecto incluye un archivo con datos iniciales (artistas, álbumes, canciones y estilos) en [data.sql](./src/main/resources/data.sql).

Una vez que el contenedor de la API haya arrancado y Hibernate haya creado las tablas (puedes verificarlo con los logs), puedes inicializar la base de datos ejecutando el siguiente comando en la consola:

```bash
docker exec -i kreitefy-db psql -U kreitefy -d kreitefy < src/main/resources/data.sql
```

*(Si prefieres hacerlo de forma visual, puedes abrir Adminer en `http://localhost:8081`, iniciar sesión e importar o pegar el contenido de `data.sql` en la pestaña **Comando SQL**).*

---

## Estructura del proyecto

```
src/
├── main/java/com/kreitefy/api/
│   ├── ApiApplication.java               ← Punto de entrada Spring Boot
│   │
│   ├── product/                          ← Módulo: catálogo musical
│   │   ├── domain/
│   │   │   └── models/                   ← Cancion, Album, Artista, EstiloMusical (Records)
│   │   ├── application/
│   │   │   ├── ports/in/                 ← Use Cases (interfaces)
│   │   │   ├── ports/out/                ← Repository Ports (interfaces)
│   │   │   └── services/                 ← Implementaciones de los Use Cases
│   │   └── infrastructure/
│   │       ├── persistence/
│   │       │   ├── adapter/              ← Adaptadores JPA (implementan los ports)
│   │       │   ├── entity/               ← Entidades @Entity
│   │       │   └── jpa/                  ← Spring Data JpaRepositories
│   │       ├── mappers/                  ← MapStruct mappers
│   │       └── rest/
│   │           ├── dtos/                 ← DTOs de request y response
│   │           └── *RestController.java  ← Controladores REST
│   │
│   ├── community/                        ← Módulo: favoritos
│   │   └── ... (misma estructura hexagonal)
│   │
│   ├── users/                            ← Módulo: auth y usuarios
│   │   ├── domain/models/                ← User, AuthToken (Records)
│   │   ├── application/
│   │   │   ├── ports/in/auth/            ← LoginUseCase, RefreshTokenUseCase
│   │   │   └── ports/out/                ← UserRepositoryPort, TokenServicePort
│   │   └── infrastructure/
│   │       ├── auth/                     ← JwtService, OAuth2 handlers
│   │       ├── config/                   ← SecurityConfig, ApplicationConfig
│   │       ├── filter/                   ← JwtAuthenticationFilter
│   │       └── persistence/              ← UserEntity, UserJpaRepository, UserRepositoryAdapter
│   │
│   └── shared/                           ← Módulo transversal (@NamedInterface)
│       ├── application/
│       │   ├── ports/in/                 ← Use Cases genéricos (CrudService, GetAll…)
│       │   └── services/                 ← CrudService<T,ID>, CrudPageableService<T,ID>
│       ├── domain/
│       │   ├── errors/                   ← NotFoundException, ConflictException…
│       │   └── models/                   ← PageInfo
│       └── infrastructure/
│           ├── mappers/                  ← EntityMapper<D,E> (interfaz base MapStruct)
│           ├── persistence/spec/         ← Especificaciones QueryDSL reutilizables
│           └── rest/exceptions/          ← GlobalExceptionHandler (@ControllerAdvice)
│
├── main/resources/
│   ├── application.properties            ← Configuración base + OAuth2
│   ├── application-dev.properties        ← H2 en fichero
│   ├── application-prod.properties       ← PostgreSQL
│   └── data.sql                          ← Datos de arranque
│
└── test/java/com/kreitefy/api/
    ├── ApiApplicationTests.java           ← Spring Modulith: verificación de módulos
    ├── ArchitectureTest.java              ← ArchUnit: 7 reglas de arquitectura
    ├── community/                         ← Tests unitarios: FavoriteSongsService
    ├── product/                           ← Tests integración: Album, Artista, Cancion repos
    ├── shared/                            ← Tests integración: GlobalExceptionHandler
    └── users/                             ← Tests unitarios + integración + E2E: auth flow
```

---

## 📐 Decisiones de diseño destacadas

### QueryDSL para consultas dinámicas
En lugar de generar múltiples métodos `findByXAndY` en los repositorios JPA, se usa **QueryDSL** con el procesador APT para generar clases Q-type (`QCancionEntity`, `QAlbumEntity`…) que permiten construir predicados tipo-seguros en runtime, equivalentes a consultas SQL dinámicas pero verificadas en compile-time.

### Java Records para el dominio
Las entidades de dominio son Java **Records** (inmutables por diseño), lo que refuerza la integridad del dominio y hace imposible modificar el estado de un objeto de dominio una vez creado, forzando a pasar por los servicios para cualquier cambio.

### Optimistic Locking con `@Version`
Para prevenir conflictos de concurrencia, todas las entidades JPA relevantes tienen un campo `@Version`. El adaptador de repositorio verifica manualmente que la versión del cliente coincide con la de la base de datos antes de guardar, lanzando `ObjectOptimisticLockingFailureException` (→ HTTP 409) en caso de conflicto.

### GlobalExceptionHandler centralizado
Un único `@ControllerAdvice` en el módulo `shared` traduce las excepciones de dominio (`NotFoundException`, `ConflictException`, `UnauthorizedException`) a respuestas HTTP con el código de estado correcto, manteniendo los controladores limpios.

---

<p align="center">
  Hecho con ❤️ durante el Bootcamp Nunsys — 2026
</p>
