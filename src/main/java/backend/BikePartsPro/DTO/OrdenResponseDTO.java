package backend.BikePartsPro.DTO;


import backend.BikePartsPro.model.EstadoOrden;
import backend.BikePartsPro.model.Orden;

import java.time.LocalDateTime;

public class OrdenResponseDTO {

    private Long id;
    private LocalDateTime fecha;
    private EstadoOrden estado;
    private Long clienteId;
    private String clienteNombre;

    public OrdenResponseDTO() {}

    public static OrdenResponseDTO desde(Orden orden) {
        OrdenResponseDTO dto = new OrdenResponseDTO();
        dto.id = orden.getId();
        dto.fecha = orden.getFecha();
        dto.estado = orden.getEstado();
        dto.clienteId = orden.getCliente().getId();
        dto.clienteNombre = orden.getCliente().getNombre();
        return dto;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public LocalDateTime getFecha() { return fecha; }
    public void setFecha(LocalDateTime fecha) { this.fecha = fecha; }

    public EstadoOrden getEstado() { return estado; }
    public void setEstado(EstadoOrden estado) { this.estado = estado; }

    public Long getClienteId() { return clienteId; }
    public void setClienteId(Long clienteId) { this.clienteId = clienteId; }

    public String getClienteNombre() { return clienteNombre; }
    public void setClienteNombre(String nombre) { this.clienteNombre = nombre; }
}