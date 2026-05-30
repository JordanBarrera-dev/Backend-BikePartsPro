package backend.BikePartsPro.model;

import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "usuarios")
public class Usuario implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // El email es el identificador único de login.
    @Column(unique = true, nullable = false)
    private String email;

    // La contraseña se guarda hasheada con BCrypt — nunca en texto plano.
    @Column(nullable = false)
    private String password;

    // El nombre del usuario para mostrar en respuestas.
    @Column(nullable = false)
    private String nombre;

    @Column(length = 20)
    private String telefono;

    // El rol determina qué endpoints puede acceder este usuario.
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Rol rol;

    // La relación es opcional porque los ADMIN no tienen Cliente asociado.
// cascade = ALL: si se elimina el Usuario, se elimina el Cliente también.
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "cliente_id", referencedColumnName = "id")
    private Cliente cliente;

    // Constructor vacío obligatorio para JPA.
    public Usuario() {}

    // Constructor completo para crear usuarios programáticamente.
    public Usuario(String email, String password, String nombre, Rol rol) {
        this.email = email;
        this.password = password;
        this.nombre = nombre;
        this.rol = rol;
    }

    // getAuthorities: Spring Security llama a este método para saber
    // qué roles tiene el usuario. Retorna una lista de GrantedAuthority.
    // SimpleGrantedAuthority("ROLE_ADMIN") es el formato que Spring espera.
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + rol.name()));
    }

    // getUsername: Spring Security usa este campo como identificador.
    // Aunque el mtodo se llama getUsername, nosotros usamos el email.
    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public String getPassword() {
        return password;
    }

    // Los tres métodos siguientes controlan el estado de la cuenta.
    // Retornan true para indicar que la cuenta está activa y habilitada.
    @Override
    public boolean isAccountNonExpired() { return true; }

    @Override
    public boolean isAccountNonLocked() { return true; }

    @Override
    public boolean isCredentialsNonExpired() { return true; }

    @Override
    public boolean isEnabled() { return true; }


    public Long getId() { return id; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public void setPassword(String password) { this.password = password; }
    public String getNombre() { return nombre; }
    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public Rol getRol() { return rol; }
    public void setRol(Rol rol) { this.rol = rol; }
    public Cliente getCliente() { return cliente; }
    public void setCliente(Cliente cliente) { this.cliente = cliente; }
}