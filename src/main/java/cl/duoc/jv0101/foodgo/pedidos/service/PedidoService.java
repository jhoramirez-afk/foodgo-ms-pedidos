package cl.duoc.jv0101.foodgo.pedidos.service;

import java.util.List;
import java.math.BigDecimal;
import cl.duoc.jv0101.foodgo.pedidos.model.DetallePedido;
import cl.duoc.jv0101.foodgo.pedidos.exception.BusinessRuleException;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import cl.duoc.jv0101.foodgo.pedidos.model.Pedido;
import cl.duoc.jv0101.foodgo.pedidos.repository.PedidoRepository;

@Service
@Transactional
public class PedidoService {

    private final PedidoRepository repository;

    public PedidoService(PedidoRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public List<Pedido> findAll() {
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Pedido> findById(Long id) {
        return repository.findById(id);
    }

    public Pedido create(Pedido recurso) {
        recurso.setId(null);
        recurso.getDetalles().forEach(item -> item.setId(null));
        recurso.setTotal(calcularTotal(recurso.getDetalles()));
        return repository.save(recurso);
    }

    public Optional<Pedido> update(Long id, Pedido datos) {
        return repository.findById(id).map(existente -> {
            existente.setCliente(datos.getCliente());
            existente.setRestaurante(datos.getRestaurante());
            existente.setTotal(calcularTotal(existente.getDetalles()));
            return repository.save(existente);
        });
    }

    public boolean delete(Long id) {
        return repository.findById(id).map(existente -> {
            repository.delete(existente);
            return true;
        }).orElse(false);
    }
    public static BigDecimal calcularTotal(List<DetallePedido> detalles) {
        BigDecimal total = detalles.stream()
                .map(item -> item.getPrecioUnitario().multiply(BigDecimal.valueOf(item.getCantidad())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        if (total.compareTo(new BigDecimal("999999999999")) > 0) {
            throw new BusinessRuleException("total", "El total supera el importe máximo admitido");
        }
        return total;
    }
}
