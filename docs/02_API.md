# API REST: pedidos

Base local: http://localhost:8083/api. Swagger UI: http://localhost:8083/swagger-ui/index.html.

| Método | Ruta | HTTP de éxito |
|---|---|---:|
| POST | /pedidos | 201 |
| GET | /pedidos | 200 |
| GET | /pedidos/{id} | 200 |
| PUT | /pedidos/{id} | 200 |
| DELETE | /pedidos/{id} | 204 |
| POST | /pedidos/{id}/detalles | 201 |
| GET | /pedidos/{id}/detalles | 200 |
| GET | /detalles/{id} | 200 |
| PUT | /detalles/{id} | 200 |
| DELETE | /detalles/{id} | 204 |

## Crear entidad principal

```json
{
  "cliente": "Camila Soto",
  "restaurante": "La Cocina de Barrio"
}
```

## Crear entidad relacionada

```json
{
  "productoId": 1,
  "nombreProducto": "Hamburguesa de vacuno con papas",
  "cantidad": 2,
  "precioUnitario": 9990
}
```

Usar el ID retornado por la creación del padre. Los ID son generados por la BD. Editar los hijos mediante sus propias rutas. Ver las reglas y los campos calculados en REGLAS_EP02.md.

Errores: 400 para datos o JSON inválidos; 404 para recurso/relación local inexistente; 409 para conflictos de integridad o unicidad cuando corresponda. Un campo demasiado largo devuelve 400. Los mensajes y validationErrors se entregan mediante ApiExceptionHandler.
