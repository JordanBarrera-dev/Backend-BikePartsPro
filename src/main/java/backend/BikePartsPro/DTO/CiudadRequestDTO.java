package backend.BikePartsPro.DTO;

public class CiudadRequestDTO {

    private String nombre;
    private Long departamentoId;

    public CiudadRequestDTO() {}

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public Long getDepartamentoId() { return departamentoId; }
    public void setDepartamentoId(Long departamentoId) { this.departamentoId = departamentoId; }
}
