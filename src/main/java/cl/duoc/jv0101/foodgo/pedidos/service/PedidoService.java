package cl.duoc.jv0101.foodgo.pedidos.service;

import java.util.List;
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
        return repository.save(recurso);
    }

    public Optional<Pedido> update(Long id, Pedido datos) {
        return repository.findById(id).map(existente -> {
            existente.setCliente(datos.getCliente());
            existente.setRestaurante(datos.getRestaurante());
            existente.setTotal(datos.getTotal());
            return repository.save(existente);
        });
    }

    public boolean delete(Long id) {
        return repository.findById(id).map(existente -> {
            repository.delete(existente);
            return true;
        }).orElse(false);
    }
}
