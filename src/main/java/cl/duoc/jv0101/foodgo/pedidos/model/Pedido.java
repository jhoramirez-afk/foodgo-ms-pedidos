package cl.duoc.jv0101.foodgo.pedidos.model;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

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
import java.math.BigDecimal;


@Entity
@Table(name = "pedidos")
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Cliente es obligatorio")
    @Size(max = 255, message = "El campo admite hasta 255 caracteres")
    @Column(nullable = false)
    private String cliente;
    @NotBlank(message = "Restaurante es obligatorio")
    @Size(max = 255, message = "El campo admite hasta 255 caracteres")
    @Column(nullable = false)
    private String restaurante;
    @com.fasterxml.jackson.annotation.JsonProperty(access = com.fasterxml.jackson.annotation.JsonProperty.Access.READ_ONLY)
    @NotNull
    @DecimalMin("0")
    @Digits(integer = 12, fraction = 0)
    @Column(nullable = false, precision = 12, scale = 0)
    private BigDecimal total = BigDecimal.ZERO;

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
