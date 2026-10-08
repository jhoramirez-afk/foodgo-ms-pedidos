# FoodGo — Microservicio Pedido

Microservicio **pedidos** de FoodGo, actualizado para la Evaluación Parcial N°2 de JVY0101.

## Responsabilidad

Gestiona el ciclo de vida del pedido y sus líneas de detalle, manteniendo referencias externas por identificador. Corresponde al requisito **RF-03** del diseño de FoodGo.

## Tecnologías

- Java 21
- Spring Boot 3.3.5
- Spring Web
- Spring Data JPA / Hibernate
- Bean Validation
- H2 para ejecución rápida local y pruebas
- MySQL 8.4 mediante perfil `mysql` y Docker Compose
- Maven
- OpenAPI / Swagger UI

## Arquitectura en capas

```text
controller -> service -> repository -> model -> base de datos
```

El dominio implementa una relación JPA bidireccional **@OneToMany / @ManyToOne** entre `Pedido` y `DetallePedido`.
Las referencias hacia otros microservicios se mantienen como identificadores (`...Id`) para evitar acoplamiento de bases de datos entre dominios.

## Endpoints REST

| Método | Endpoint | Resultado |
|---|---|---|
| GET | `/api/pedidos` | Listar pedidos |
| GET | `/api/pedidos/{id}` | Obtener por id |
| POST | `/api/pedidos` | Crear recurso |
| PUT | `/api/pedidos/{id}` | Actualizar recurso |
| DELETE | `/api/pedidos/{id}` | Eliminar recurso |
| GET | `/api/pedidos/{pedidoId}/detalles` | Listar recursos relacionados |
| POST | `/api/pedidos/{pedidoId}/detalles` | Crear recurso relacionado |
| GET | `/api/detalles/{id}` | Obtener recurso relacionado |
| PUT | `/api/detalles/{id}` | Actualizar recurso relacionado |
| DELETE | `/api/detalles/{id}` | Eliminar recurso relacionado |

### Ejemplo de creación de Pedido

```json
{
  "cliente": "Cliente EP02",
  "restaurante": "Restaurante EP02",
  "total": 12990
}
```

### Ejemplo de creación de DetallePedido

```json
{
  "productoId": 1,
  "nombreProducto": "Hamburguesa",
  "cantidad": 2,
  "precioUnitario": 9990.0
}
```

## Respuestas de error

- `400 Bad Request`: validación de campos.
- `404 Not Found`: identificador inexistente.
- `409 Conflict`: violación de integridad o restricción única.

Los errores se entregan en JSON mediante `@RestControllerAdvice`.

## Ejecución rápida con H2

Requisitos: JDK 21 y Maven 3.9+.

```bash
git clone https://github.com/jhoramirez-afk/foodgo-ms-pedidos.git
cd foodgo-ms-pedidos
git switch develop
mvn clean install
mvn spring-boot:run
```

Servicio: `http://localhost:8083`
Swagger UI: `http://localhost:8083/swagger-ui/index.html`
H2 Console: `http://localhost:8083/h2-console`

JDBC H2: `jdbc:h2:file:./data/foodgo_pedidos`
Usuario: `sa`
Contraseña: vacía.

## Ejecución con MySQL

```bash
docker compose up --build
```

El `docker-compose.yml` levanta el microservicio y una base MySQL independiente para el dominio.

## Maven y empaquetado

```bash
mvn clean
mvn test
mvn install
mvn package
java -jar target/pedidos-svc-2.0.0.jar
```

Después de `mvn package` debe existir un archivo `.jar` válido en `target/`.

## Postman

La carpeta `postman/` contiene una colección con casos correctos y casos de error. Puede importarse directamente en Postman.

## Estrategia Git

- `main`: versión estable.
- `develop`: integración de la EP02.
- `feature/jpa-relations`: entidades y relaciones JPA.
- `feature/crud-errors`: CRUD y manejo uniforme de errores.
- `feature/persistence-tests-docs`: conexión relacional MySQL, Docker y documentación reproducible.

Los cambios deben integrarse mediante commits descriptivos y, de ser posible, Pull Requests.

## Persistencia y pruebas de integración

El perfil local H2 guarda los datos en `data/` y los conserva al reiniciar. No se versiona esa carpeta. Las pruebas usan el perfil `test` con una BD independiente en memoria y comprueban CRUD de ambas entidades, relaciones, eliminación en cascada, validación y recursos inexistentes mediante HTTP (MockMvc).

Ejecutar `mvn clean install` para compilar, ejecutar las pruebas y generar el JAR.

Se conserva el contrato de campos de EP01. JaCoCo verifica un mínimo de 80% de líneas del código de aplicación (excluye el arranque), además de producir el informe. Se mantienen las pruebas unitarias y los escenarios Cucumber existentes.

La guía `docs/DEMO_EP02.md` incluye SQL, persistencia tras reiniciar y un guion para el video. Ejecutar la colección Postman en orden: usa IDs reales y verifica HTTP, errores y actualizaciones.

## Revisión de la actualización

La EP02 se propone desde `develop` hacia `main` mediante un pull request. El propietario revisa los cambios y ejecuta las pruebas antes de fusionarlo. Mientras el PR permanezca abierto, clonar y ejecutar `git switch develop` para probar la EP02. Los commits conservan fechas reales del desarrollo.
