# language: es
Característica: Servicio Pedido (microservicio pedidos del caso FoodGo)
  Los escenarios validan el contrato REST del microservicio alineado a sus endpoints.

  Escenario: el listado del recurso responde 200
    Dado el servicio "Pedido" está disponible
    Cuando consulto el listado de "pedidos"
    Entonces el listado responde con código 200

  Escenario: ciclo de vida completo del recurso
    Dado un nuevo "pedido" con cliente "hola-cucumber"
    Cuando consulto el "pedido" recién creado
    Entonces el recurso tiene cliente "hola-cucumber" y código 200
    Cuando actualizo el "pedido" con cliente "cucumber-actualizado"
    Entonces el recurso queda con cliente "cucumber-actualizado" y código 200
    Cuando elimino el "pedido"
    Entonces la eliminación responde con código 204
    Y al consultar el "pedido" eliminado responde 404
