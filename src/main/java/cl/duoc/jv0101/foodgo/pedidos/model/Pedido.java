package cl.duoc.jv0101.foodgo.pedidos.model;

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

    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public String getCliente() { return cliente; }

    public void setCliente(String cliente) { this.cliente = cliente; }

    public String getRestaurante() { return restaurante; }

    public void setRestaurante(String restaurante) { this.restaurante = restaurante; }

    public BigDecimal getTotal() { return total; }

    public void setTotal(BigDecimal total) { this.total = total; }

}
