package cl.duoc.jv0101.foodgo.pedidos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import cl.duoc.jv0101.foodgo.pedidos.model.Pedido;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {
}
