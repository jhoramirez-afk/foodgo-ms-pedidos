package cl.duoc.jv0101.foodgo.pedidos.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import cl.duoc.jv0101.foodgo.pedidos.model.Pedido;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {
    @Override
    @EntityGraph(attributePaths = "detalles")
    List<Pedido> findAll();

    @Override
    @EntityGraph(attributePaths = "detalles")
    Optional<Pedido> findById(Long id);
}
