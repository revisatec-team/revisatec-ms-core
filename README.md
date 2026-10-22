# MS1 - Revisatec Core

Microservicio de gestión académica para cursos, grupos y eventos de calendario.

## Stack y arquitectura

- Java 21, Spring Boot 4.1.1, Maven, Spring MVC, Spring Data JPA y Bean Validation.
- H2 en memoria para desarrollo local; SQL Server/Azure SQL con el perfil `azure`.
- Arquitectura hexagonal bajo `cr.tec.revisatec.mscore`: `domain`, `application/port/in`, `application/port/out`, `application/service` e `infrastructure/web` y `infrastructure/persistence` por recurso.

## Configuración

| Variable | Uso |
| --- | --- |
| `PORT` | Puerto HTTP; por defecto `8080`. |
| `AZURE_SQL_URL` | URL JDBC de Azure SQL; se usa con el perfil `azure`. |
| `AZURE_SQL_USERNAME` | Usuario de Azure SQL. |
| `AZURE_SQL_PASSWORD` | Contraseña de Azure SQL. |

Las credenciales deben configurarse como variables de entorno o Azure App Settings; no guardes valores reales en archivos. [`.env.example`](./.env.example) contiene solo nombres sin valores y `.env` está excluido por Git.

## Ejecución

Local con H2 (configuración por defecto):

```bash
./mvnw spring-boot:run
```

Con Azure SQL y las variables anteriores definidas:

```bash
SPRING_PROFILES_ACTIVE=azure ./mvnw spring-boot:run
```

En Azure App Service configura `SPRING_PROFILES_ACTIVE=azure`, `AZURE_SQL_URL`, `AZURE_SQL_USERNAME`, `AZURE_SQL_PASSWORD` y, si aplica, `PORT` en Application settings.

Ejecutar pruebas:

```bash
./mvnw test
```

## Despliegue y acceso

El workflow de GitHub Actions construye y despliega a Azure App Service al hacer push a `main` (también permite ejecución manual). El acceso a la API debe realizarse únicamente a través de APIM con mTLS; esa configuración de entrada se administra fuera de este repositorio. Las llamadas mediante APIM usan el header `Ocp-Apim-Subscription-Key`.

## API

La especificación OpenAPI está en [`openapi.yaml`](./openapi.yaml). Las rutas listadas usan el prefijo indicado.

| Método | Ruta | Resultado |
| --- | --- | --- |
| GET | `/courses` | Cursos paginados y filtrados por nombre. |
| POST | `/courses` | Crea curso (201). |
| GET | `/courses/{id}` | Obtiene curso (200/404). |
| PUT | `/courses/{id}` | Actualiza curso (200/404). |
| DELETE | `/courses/{id}` | Elimina curso (200 sin cuerpo/404). |
| GET | `/groups` | Grupos paginados y filtrados por nombre; incluye `avance` y `estado`. |
| POST | `/groups` | Crea grupo (201). |
| GET | `/groups/{id}` | Obtiene grupo (200/404). |
| PUT | `/groups/{id}` | Actualiza grupo (200/404). |
| DELETE | `/groups/{id}` | Elimina grupo (200 sin cuerpo/404). |
| GET | `/calendar/events` | Eventos paginados, filtrados por título y ordenados por fecha ascendente. |
| POST | `/calendar/events` | Crea evento (201). |
| PUT | `/calendar/events/{id}` | Actualiza evento (200/404). |
| DELETE | `/calendar/events/{id}` | Elimina evento (200 con `{"message":"Evento eliminado"}`/404). |

### Convenciones

- Listados aceptan `page` (inicia en 1), `pageSize` (1 a 100) y `search` (coincidencia sin distinguir mayúsculas en nombre o título). Calendario devuelve 400 si la página excede el rango; cursos y grupos devuelven una página vacía si no hay resultados para esa página.
- Validaciones y errores usan JSON con forma `{"error":"mensaje"}` y código 400; recursos inexistentes responden 404.
- Las fechas de calendario son `Long` Epoch en milisegundos, tanto en solicitudes como en respuestas.
- Los campos `estado` del grupo admiten `Pendiente`, `En progreso` o `Completado`; `type` de calendario admite `blue`, `green` o `amber`.
