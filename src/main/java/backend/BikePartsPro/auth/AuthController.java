package backend.BikePartsPro.auth;

import backend.BikePartsPro.model.Cliente;
import backend.BikePartsPro.model.Rol;
import backend.BikePartsPro.model.Usuario;
import backend.BikePartsPro.repository.UsuarioRepository;
import backend.BikePartsPro.security.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

// AuthController expone los dos únicos endpoints públicos de la API:
// POST /auth/register → crea un usuario nuevo
// POST /auth/login    → valida credenciales y devuelve el token JWT
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    public AuthController(UsuarioRepository usuarioRepository,
                          PasswordEncoder passwordEncoder,
                          AuthenticationManager authenticationManager,
                          JwtUtil jwtUtil) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    // register: recibe los datos del nuevo usuario, hashea la contraseña
    // y guarda el usuario en la base de datos.
    // Si el email ya existe, retorna 400 Bad Request.
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequestDTO request) {
        if (usuarioRepository.findByEmail(request.getEmail()).isPresent()) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "El email ya esta registrado"));
        }

        Usuario usuario = new Usuario(
                request.getEmail(),
                passwordEncoder.encode(request.getPassword()),
                request.getNombre(),
                Rol.CLIENTE  // siempre CLIENTE — el rol no viene del cliente
        );

        // Todo registro crea su Cliente asociado automáticamente.
        Cliente cliente = new Cliente(request.getNombre(), request.getEmail());
        usuario.setCliente(cliente);

        usuarioRepository.save(usuario);

        return ResponseEntity.ok(Map.of(
                "mensaje", "Usuario registrado exitosamente",
                "email", usuario.getEmail(),
                "rol", usuario.getRol()
        ));
    }

    // login: recibe email y contraseña, delega la verificación al
    // AuthenticationManager, y si son correctas genera y devuelve el token.
    // Si las credenciales son incorrectas, Spring Security lanza una excepción
    // que retorna automáticamente 401 Unauthorized.
//    @PostMapping("/login")
//    public ResponseEntity<?> login(@RequestBody LoginRequestDTO request) {
//
//        // authenticate lanza BadCredentialsException si las credenciales no coinciden.
//        Authentication authentication = authenticationManager.authenticate(
//                new UsernamePasswordAuthenticationToken(
//                        request.getEmail(),
//                        request.getPassword()
//                )
//        );
//
//        // Si llegamos aquí, la autenticación fue exitosa.
//        // getPrincipal() retorna el UserDetails del usuario autenticado.
//        Usuario usuario = (Usuario) authentication.getPrincipal();
//
//        // Generar el token JWT con los datos del usuario.
//        String token = jwtUtil.generateToken(usuario);
//
//        return ResponseEntity.ok(Map.of(
//                "token", token,
//                "email", usuario.getEmail(),
//                "rol", usuario.getRol(),
//                "nombre", usuario.getNombre()
//        ));
//    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDTO request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );
            Usuario usuario = (Usuario) authentication.getPrincipal();
            System.out.println("Usuario autenticado: " + usuario.getEmail());
            String token = jwtUtil.generateToken(usuario);
            System.out.println("Token generado: " + token);
            return ResponseEntity.ok(Map.of(
                    "token", token,
                    "email", usuario.getEmail(),
                    "rol", usuario.getRol(),
                    "nombre", usuario.getNombre()
            ));
        } catch (Exception e) {
            System.out.println("ERROR EN LOGIN: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }


    @PutMapping("/promover/{email}")
    public ResponseEntity<?> promoverAdmin(@PathVariable String email) {
        Usuario usuario = usuarioRepository.findByEmail(email).orElse(null);
        if (usuario == null) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Usuario no encontrado"));
        }
        usuario.setRol(Rol.ADMIN);
        usuarioRepository.save(usuario);
        return ResponseEntity.ok(Map.of(
                "mensaje", "Usuario promovido a ADMIN",
                "email", usuario.getEmail(),
                "rol", usuario.getRol()
        ));
    }
}