package backend.BikePartsPro.DTO;


import backend.BikePartsPro.model.OrdenItem;

public class OrdenItemResponseDTO {

    private Long id;
    private Long ordenId;
    private Long productoId;
    private String productoNombre;
    private Integer cantidad;
    private Double precioUnitario;

    public OrdenItemResponseDTO() {}

    public static OrdenItemResponseDTO desde(OrdenItem item) {
        OrdenItemResponseDTO dto = new OrdenItemResponseDTO();
        dto.id = item.getId();
        dto.ordenId = item.getOrden().getId();
        dto.productoId = item.getProducto().getId();
        dto.productoNombre = item.getProducto().getNombre();
        dto.cantidad = item.getCantidad();
        dto.precioUnitario = item.getPrecioUnitario();
        return dto;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getOrdenId() { return ordenId; }
    public void setOrdenId(Long ordenId) { this.ordenId = ordenId; }

    public Long getProductoId() { return productoId; }
    public void setProductoId(Long productoId) { this.productoId = productoId; }

    public String getProductoNombre() { return productoNombre; }
    public void setProductoNombre(String productoNombre) { this.productoNombre = productoNombre; }

    public Integer getCantidad() { return cantidad; }
    public void setCantidad(Integer cantidad) { this.cantidad = cantidad; }

    public Double getPrecioUnitario() { return precioUnitario; }
    public void setPrecioUnitario(Double precioUnitario) { this.precioUnitario = precioUnitario; }
}

