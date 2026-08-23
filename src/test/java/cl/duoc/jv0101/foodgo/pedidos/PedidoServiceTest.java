package cl.duoc.jv0101.foodgo.pedidos;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import cl.duoc.jv0101.foodgo.pedidos.model.Pedido;
import cl.duoc.jv0101.foodgo.pedidos.repository.PedidoRepository;
import cl.duoc.jv0101.foodgo.pedidos.service.PedidoService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PedidoServiceTest {

    @Mock
    private PedidoRepository repository;

    @InjectMocks
    private PedidoService service;

    private Pedido recurso() {
        Pedido r = new Pedido();
        r.setId(1L);
        r.setCliente("Demo");
        r.setRestaurante("valor");
        r.setTotal(BigDecimal.TEN);
        return r;
    }

    @Test
    void listarRetornaTodos() {
        when(repository.findAll()).thenReturn(List.of(recurso()));
        assertThat(service.findAll()).hasSize(1);
    }

    @Test
    void buscarPorIdExistente() {
        when(repository.findById(1L)).thenReturn(Optional.of(recurso()));
        assertThat(service.findById(1L)).isPresent();
    }

    @Test
    void buscarPorIdInexistente() {
        when(repository.findById(9L)).thenReturn(Optional.empty());
        assertThat(service.findById(9L)).isEmpty();
    }

    @Test
    void crearGuarda() {
        when(repository.save(any())).thenReturn(recurso());
        assertThat(service.create(recurso()).getCliente()).isEqualTo("Demo");
    }

    @Test
    void actualizarExistente() {
        Pedido datos = recurso();
        datos.setCliente("Actualizado");
        when(repository.findById(1L)).thenReturn(Optional.of(recurso()));
        when(repository.save(any())).thenAnswer(inv -> inv.getArgument(0));
        Optional<Pedido> resultado = service.update(1L, datos);
        assertThat(resultado).isPresent();
        assertThat(resultado.get().getCliente()).isEqualTo("Actualizado");
    }

    @Test
    void actualizarInexistente() {
        when(repository.findById(9L)).thenReturn(Optional.empty());
        assertThat(service.update(9L, recurso())).isEmpty();
    }

    @Test
    void eliminarExistente() {
        when(repository.findById(1L)).thenReturn(Optional.of(recurso()));
        assertThat(service.delete(1L)).isTrue();
        verify(repository).delete(any());
    }

    @Test
    void eliminarInexistente() {
        when(repository.findById(9L)).thenReturn(Optional.empty());
        assertThat(service.delete(9L)).isFalse();
    }
}
