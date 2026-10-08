package cl.duoc.jv0101.foodgo.pedidos.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.CascadeType;
import jakarta.persistence.OneToMany;
import jakarta.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import java.math.BigDecimal;


@Entity
@Table(name = "pedidos")
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El cliente es obligatorio")
    @Column(nullable = false)
    private String cliente;
    @Column
    private String restaurante;
    @Column
    private BigDecimal total;

    @Valid
    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference("pedido-detalles")
    private List<DetallePedido> detalles = new ArrayList<>();

    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public String getCliente() { return cliente; }

    public void setCliente(String cliente) { this.cliente = cliente; }

    public String getRestaurante() { return restaurante; }

    public void setRestaurante(String restaurante) { this.restaurante = restaurante; }

    public BigDecimal getTotal() { return total; }

    public void setTotal(BigDecimal total) { this.total = total; }

    public List<DetallePedido> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<DetallePedido> items) {
        this.detalles.clear();
        if (items != null) {
            items.forEach(this::addDetallePedido);
        }
    }

    public void addDetallePedido(DetallePedido item) {
        detalles.add(item);
        item.setPedido(this);
    }

    public void removeDetallePedido(DetallePedido item) {
        detalles.remove(item);
        item.setPedido(null);
    }
}
