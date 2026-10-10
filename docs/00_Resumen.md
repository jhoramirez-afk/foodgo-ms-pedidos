# Pedido — Resumen del microservicio

## Propósito

Administra el carrito, la creación y el ciclo de vida del pedido: es el dominio transaccional central de foodgo.

## Contexto

- **Caso**: FoodGo (delivery de comida a domicilio)
- **Microservicio**: pedidos
- **Base path**: `/api/pedidos`

## Responsabilidad única (SRP)

Ciclo de vida del pedido. El servicio atiende un único dominio de negocio y tiene una sola razón de cambio. Entrega su propia base de datos en memoria (H2) y expone su API REST de forma independiente, garantizando **bajo acoplamiento** y **alta cohesión** dentro de la arquitectura de microservicios del caso.

## Requisitos del caso que cubre

RF-03 (armar y confirmar pedidos), RNF-01 (escalabilidad en picos de almuerzo/cena), RNF-06 (consistencia: persistencia + cola antes de confirmar)

## Stack tecnológico

| Componente | Tecnología |
|---|---|
| Framework | Spring Boot 3.3 |
| Lenguaje | Java 21 |
| Build | Maven |
| Persistencia | Spring Data JPA + H2 de pruebas y MySQL para la demostración relacional |
| Validación | Bean Validation (`jakarta.validation`) |
| API/Docs | springdoc-openapi — Swagger UI + OpenAPI yaml + ReDoc |
| Calidad | JaCoCo (umbral mínimo LINE 80%) + Cucumber (BDD REST) |
| Contenedores | Docker + Docker Compose |

## Entradas disponibles desde la web (`/`)

La página raíz presenta el servicio y enlaza Swagger UI, OpenAPI yaml, ReDoc y la consola H2.
