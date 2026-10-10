# Integridad del dominio pedidos

Cliente y restaurante obligatorios. Cada detalle exige producto positivo, cantidad de al menos 1 y precio positivo. `total` es de solo lectura: se calcula al crear el pedido y al crear, modificar o eliminar detalles. Un pedido sin detalles tiene total 0. PUT del pedido actualiza cliente y restaurante; los detalles se editan en sus propias rutas.

## Alcance de la evaluación

Se mantienen controller/service/repository/model, CRUD REST, relaciones OneToMany/ManyToOne, MySQL, Maven y Git. Las reglas hacen coherentes los datos retornados y las pruebas de éxito/error (IE1, IE2, IE3, IE5, IE6). No se agregan componentes externos. README, Postman y consultas SQL respaldan IE4, IE7 e IE10.

La colección incluye 9 peticiones de CRUD/lectura, 10 casos de error y 6 peticiones de eliminación/cascada. `mvn clean install` ejecuta pruebas unitarias, MockMvc con JPA/H2 y escenarios Cucumber. MySQL se demuestra con el perfil mysql y la colección.
