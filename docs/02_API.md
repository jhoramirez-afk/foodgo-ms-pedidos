# Pedido — Contrato de la API REST

## Base

- **Base path**: `/api/pedidos`
- **Formato**: JSON — **Puerto**: 8083 (configurable con `PORT`)

## Recursos

| Método | Ruta | Códigos de estado | Descripción |
|--------|------|-------------------|-------------|
| GET | `/api/pedidos` | 200 | Lista todos los recursos |
| GET | `/api/pedidos/{id}` | 200 / 404 | Obtiene un recurso por id |
| POST | `/api/pedidos` | 201 / 400 | Crea un recurso |
| PUT | `/api/pedidos/{id}` | 200 / 404 / 400 | Actualiza un recurso |
| DELETE | `/api/pedidos/{id}` | 204 / 404 | Elimina un recurso |

## Atributos de un recurso

| Campo | Tipo | Obligatorio | Descripción |
|-------|------|-------------|-------------|
| id | Long | - | Identificador autogenerado |
| cliente | String | Sí | Campo principal del recurso |
| restaurante | String | No | Campo del dominio |
| total | BigDecimal | No | Campo del dominio |

## Ejemplos con curl

```bash
# Listar
curl http://localhost:8083/api/pedidos

# Crear
curl -X POST http://localhost:8083/api/pedidos \
  -H "Content-Type: application/json" \
  -d '{"cliente":"Demo"}'

# Obtener por id
curl http://localhost:8083/api/pedidos/1

# Actualizar
curl -X PUT http://localhost:8083/api/pedidos/1 \
  -H "Content-Type: application/json" \
  -d '{"cliente":"Actualizado"}'

# Eliminar
curl -X DELETE http://localhost:8083/api/pedidos/1
```
