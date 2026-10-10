package cl.duoc.jv0101.foodgo.pedidos.model;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;

@Entity
@Table(name = "detalles_pedido")
public class DetallePedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Producto es obligatorio")
    @Positive(message = "Producto debe ser mayor que cero")
    @Column(nullable = false)
    private Long productoId;

    @NotBlank(message = "Nombre del producto es obligatorio")
    @Size(max = 255, message = "El campo admite hasta 255 caracteres")
    @Column(nullable = false)
    private String nombreProducto;

    @NotNull(message = "Cantidad es obligatoria")
    @Min(value = 1, message = "La cantidad mínima es 1")
    @Column(nullable = false)
    private Integer cantidad;

    @NotNull(message = "Precio unitario es obligatorio")
    @DecimalMin(value = "1", message = "Precio unitario debe ser mayor que cero")
    @Digits(integer = 9, fraction = 0, message = "El importe debe expresarse en pesos CLP enteros, hasta 9 dígitos")
    @Column(precision = 9, scale = 0)
    private BigDecimal precioUnitario;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "pedido_id", nullable = false)
    @JsonBackReference("pedido-detalles")
    private Pedido pedido;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getProductoId() {
        return productoId;
    }

    public void setProductoId(Long productoId) {
        this.productoId = productoId;
    }

    public String getNombreProducto() {
        return nombreProducto;
    }

    public void setNombreProducto(String nombreProducto) {
        this.nombreProducto = nombreProducto;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public BigDecimal getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(BigDecimal precioUnitario) {
        this.precioUnitario = precioUnitario;
    }

    public Pedido getPedido() {
        return pedido;
    }

    public void setPedido(Pedido pedido) {
        this.pedido = pedido;
    }
}
