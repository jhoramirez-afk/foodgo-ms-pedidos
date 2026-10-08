package cl.duoc.jv0101.foodgo.pedidos.controller;

import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import cl.duoc.jv0101.foodgo.pedidos.model.DetallePedido;
import cl.duoc.jv0101.foodgo.pedidos.service.DetallePedidoService;

@RestController
@RequestMapping("/api")
public class DetallePedidoController {

    private final DetallePedidoService service;

    public DetallePedidoController(DetallePedidoService service) {
        this.service = service;
    }

    @GetMapping("/pedidos/{pedidoId}/detalles")
    public ResponseEntity<List<DetallePedido>> listarPorPedido(@PathVariable Long pedidoId) {
        return ResponseEntity.ok(service.findByPedidoId(pedidoId));
    }

    @PostMapping("/pedidos/{pedidoId}/detalles")
    public ResponseEntity<DetallePedido> crear(@PathVariable Long pedidoId, @Valid @RequestBody DetallePedido recurso) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(pedidoId, recurso));
    }

    @GetMapping("/detalles/{id}")
    public ResponseEntity<DetallePedido> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @PutMapping("/detalles/{id}")
    public ResponseEntity<DetallePedido> actualizar(@PathVariable Long id, @Valid @RequestBody DetallePedido datos) {
        return ResponseEntity.ok(service.update(id, datos));
    }

    @DeleteMapping("/detalles/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
