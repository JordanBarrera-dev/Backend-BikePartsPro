package backend.BikePartsPro.DTO;

public class EnvioRequestDTO {

    private String nombreRecibe;
    private String direccion;
    private String complemento;
    private String codigoPostal;
    private Long ciudadId;
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
