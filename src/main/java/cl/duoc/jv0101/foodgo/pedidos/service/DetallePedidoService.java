package cl.duoc.jv0101.foodgo.pedidos.service;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import cl.duoc.jv0101.foodgo.pedidos.exception.ResourceNotFoundException;
import cl.duoc.jv0101.foodgo.pedidos.model.DetallePedido;
import cl.duoc.jv0101.foodgo.pedidos.model.Pedido;
import cl.duoc.jv0101.foodgo.pedidos.repository.DetallePedidoRepository;
import cl.duoc.jv0101.foodgo.pedidos.repository.PedidoRepository;

@Service
@Transactional
public class DetallePedidoService {

    private final DetallePedidoRepository repository;
    private final PedidoRepository pedidoRepository;

    public DetallePedidoService(DetallePedidoRepository repository, PedidoRepository pedidoRepository) {
        this.repository = repository;
        this.pedidoRepository = pedidoRepository;
    }

    @Transactional(readOnly = true)
    public List<DetallePedido> findByPedidoId(Long pedidoId) {
        if (!pedidoRepository.existsById(pedidoId)) {
            throw new ResourceNotFoundException("Pedido no encontrado con id " + pedidoId);
        }
        return repository.findByPedido_Id(pedidoId);
    }

    @Transactional(readOnly = true)
    public DetallePedido findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("DetallePedido no encontrado con id " + id));
    }

    public DetallePedido create(Long pedidoId, DetallePedido recurso) {
        Pedido pedido = pedidoRepository.findById(pedidoId)
                .orElseThrow(() -> new ResourceNotFoundException("Pedido no encontrado con id " + pedidoId));
        recurso.setId(null);
        recurso.setPedido(pedido);
        DetallePedido guardado = repository.save(recurso);
        recalcularTotal(pedido);
        return guardado;
    }

    public DetallePedido update(Long id, DetallePedido datos) {
        DetallePedido existente = findById(id);
        existente.setProductoId(datos.getProductoId());
        existente.setNombreProducto(datos.getNombreProducto());
        existente.setCantidad(datos.getCantidad());
        existente.setPrecioUnitario(datos.getPrecioUnitario());
        DetallePedido guardado = repository.save(existente);
        recalcularTotal(existente.getPedido());
        return guardado;
    }

    public void delete(Long id) {
        DetallePedido existente = findById(id);
        Pedido pedido = existente.getPedido();
        repository.delete(existente);
        recalcularTotal(pedido);
    }
    private void recalcularTotal(Pedido pedido) {
        pedido.setTotal(PedidoService.calcularTotal(repository.findByPedido_Id(pedido.getId())));
        // La entidad está administrada; JPA persiste el cambio al confirmar la transacción.
    }
}
