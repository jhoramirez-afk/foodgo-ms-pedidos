package cl.duoc.jv0101.foodgo.pedidos.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import cl.duoc.jv0101.foodgo.pedidos.model.DetallePedido;

public interface DetallePedidoRepository extends JpaRepository<DetallePedido, Long> {
    List<DetallePedido> findByPedido_Id(Long pedidoId);
}
