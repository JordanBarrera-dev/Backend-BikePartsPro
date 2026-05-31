package backend.BikePartsPro.auth;

public class VerificarRegistroRequestDTO {

    private String email;
    private String codigo;

    public VerificarRegistroRequestDTO() {}

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }
}
