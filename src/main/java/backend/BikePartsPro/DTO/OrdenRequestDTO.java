package backend.BikePartsPro.DTO;


import backend.BikePartsPro.model.EstadoOrden;

import java.time.LocalDateTime;

public class OrdenRequestDTO {

    private LocalDateTime fecha;
    private EstadoOrden estado;
    private Long clienteId;

    public OrdenRequestDTO() {}

    public LocalDateTime getFecha() { return fecha; }
    public void setFecha(LocalDateTime fecha) { this.fecha = fecha; }

    public EstadoOrden getEstado() { return estado; }
    public void setEstado(EstadoOrden estado) { this.estado = estado; }

    public Long getClienteId() { return clienteId; }
    public void setClienteId(Long clienteId) { this.clienteId = clienteId; }
}