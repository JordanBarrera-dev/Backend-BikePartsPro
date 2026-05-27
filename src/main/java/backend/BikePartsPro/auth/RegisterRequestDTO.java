package backend.BikePartsPro.auth;

// RegisterRequestDTO: los datos que el cliente debe enviar para registrarse.
// Separar este DTO del modelo Usuario evita exponer campos internos
// como el id o la contraseña hasheada en la respuesta.
public class RegisterRequestDTO {

    private String nombre;
    private String email;
    private String password;

    public RegisterRequestDTO() {}

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}
