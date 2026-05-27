package backend.BikePartsPro.DTO;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class OrdenItemRequestDTO {


    private Long ordenId;
    private Long productoId;

    @NotNull(message = "No queda vaico")
    @Min(value = 1, message = "Cantidad minn es 1")
    private Integer cantidad;
    private Double precioUnitario;

    public OrdenItemRequestDTO() {}

    public Long getOrdenId() { return ordenId; }
    public void setOrdenId(Long ordenId) { this.ordenId = ordenId; }

    public Long getProductoId() { return productoId; }
    public void setProductoId(Long productoId) { this.productoId = productoId; }

    public Integer getCantidad() { return cantidad; }
    public void setCantidad(Integer cantidad) { this.cantidad = cantidad; }

    public Double getPrecioUnitario() { return precioUnitario; }
    public void setPrecioUnitario(Double precioUnitario) { this.precioUnitario = precioUnitario; }
}
