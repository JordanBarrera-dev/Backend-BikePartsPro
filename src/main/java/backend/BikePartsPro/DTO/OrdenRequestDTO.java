package backend.BikePartsPro.DTO;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public class OrdenRequestDTO {

    @NotNull(message = "El clienteId es obligatorio")
    private Long clienteId;

    @NotEmpty(message = "La orden debe tener al menos un producto")
    @Valid
    private List<OrdenItemRequestDTO> items;

    public OrdenRequestDTO() {}

    public Long getClienteId() { return clienteId; }
    public void setClienteId(Long clienteId) { this.clienteId = clienteId; }

    public List<OrdenItemRequestDTO> getItems() { return items; }
    public void setItems(List<OrdenItemRequestDTO> items) { this.items = items; }
}