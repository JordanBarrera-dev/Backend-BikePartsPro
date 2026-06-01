package backend.BikePartsPro.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class EnvioRequestDTO {

    @NotBlank(message = "El nombre de quien recibe es obligatorio")
    private String nombreRecibe;

    @NotBlank(message = "La dirección es obligatoria")
    private String direccion;

    private String complemento;

    private String codigoPostal;

    @NotNull(message = "La ciudad es obligatoria")
    private Long ciudadId;

    @NotNull(message = "El cliente es obligatorio")
    private Long clienteId;

    public EnvioRequestDTO() {}

    public String getNombreRecibe() { return nombreRecibe; }
    public void setNombreRecibe(String nombreRecibe) { this.nombreRecibe = nombreRecibe; }

    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }

    public String getComplemento() { return complemento; }
    public void setComplemento(String complemento) { this.complemento = complemento; }

    public String getCodigoPostal() { return codigoPostal; }
    public void setCodigoPostal(String codigoPostal) { this.codigoPostal = codigoPostal; }

    public Long getCiudadId() { return ciudadId; }
    public void setCiudadId(Long ciudadId) { this.ciudadId = ciudadId; }

    public Long getClienteId() { return clienteId; }
    public void setClienteId(Long clienteId) { this.clienteId = clienteId; }
}
