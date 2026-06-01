package backend.BikePartsPro.auth;

import backend.BikePartsPro.model.Cliente;
import backend.BikePartsPro.model.Rol;
import backend.BikePartsPro.model.Usuario;
import backend.BikePartsPro.repository.UsuarioRepository;
import backend.BikePartsPro.security.JwtUtil;
import backend.BikePartsPro.service.EmailService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final EmailService emailService;

    public AuthController(UsuarioRepository usuarioRepository,
                          PasswordEncoder passwordEncoder,
                          AuthenticationManager authenticationManager,
                          JwtUtil jwtUtil,
                          EmailService emailService) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.emailService = emailService;
    }

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
                Rol.CLIENTE
        );

        String codigo = String.format("%06d", new Random().nextInt(1000000));
        usuario.setTokenRegistro(passwordEncoder.encode(codigo));
        usuario.setTokenRegistroExpiracion(LocalDateTime.now().plusMinutes(15));

        usuarioRepository.save(usuario);

        emailService.enviarCodigoVerificacion(usuario.getEmail(), codigo);

        return ResponseEntity.ok(Map.of(
                "mensaje", "Usuario registrado. Revisa tu email para el código de verificación.",
                "email", usuario.getEmail(),
                "rol", usuario.getRol()
        ));
    }

    @PostMapping("/verificar-registro")
    public ResponseEntity<?> verificarRegistro(@RequestBody VerificarRegistroRequestDTO request) {
        Usuario usuario = usuarioRepository.findByEmail(request.getEmail()).orElse(null);
        if (usuario == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "Usuario no encontrado"));
        }
        if (usuario.isVerificado()) {
            return ResponseEntity.ok(Map.of("mensaje", "El usuario ya está verificado"));
        }
        if (usuario.getTokenRegistroExpiracion() == null ||
            usuario.getTokenRegistroExpiracion().isBefore(LocalDateTime.now())) {
            return ResponseEntity.badRequest().body(Map.of("error", "El código ha expirado"));
        }
        if (!passwordEncoder.matches(request.getCodigo(), usuario.getTokenRegistro())) {
            return ResponseEntity.badRequest().body(Map.of("error", "Código incorrecto"));
        }

        Cliente cliente = new Cliente(usuario.getNombre(), usuario.getEmail());
        usuario.setCliente(cliente);
        usuario.setVerificado(true);
        usuario.setTokenRegistro(null);
        usuario.setTokenRegistroExpiracion(null);
        usuarioRepository.save(usuario);

        return ResponseEntity.ok(Map.of("mensaje", "Cuenta verificada correctamente"));
    }

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
            String token = jwtUtil.generateToken(usuario);
            Map<String, Object> response = new HashMap<>();
            response.put("token", token);
            response.put("email", usuario.getEmail());
            response.put("rol", usuario.getRol());
            response.put("nombre", jwtUtil.extractNombre(token));
            response.put("verificado", usuario.isVerificado());
            response.put("userId", usuario.getId());
            if (usuario.getCliente() != null) {
                response.put("clienteId", usuario.getCliente().getId());
            }
            return ResponseEntity.ok(response);
        } catch (DisabledException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("error", "Cuenta no verificada. Revisa tu email para activar tu cuenta."));
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Credenciales incorrectas"));
        }
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken() {
        Usuario usuario = (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String nuevoToken = jwtUtil.generateToken(usuario);
        return ResponseEntity.ok(Map.of("token", nuevoToken));
    }

    @GetMapping("/validar-token")
    public ResponseEntity<?> validarToken() {
        Usuario usuario = (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Map<String, Object> response = new HashMap<>();
        response.put("valido", true);
        response.put("email", usuario.getEmail());
        response.put("rol", usuario.getRol());
        response.put("nombre", usuario.getNombre().trim().split("\\s+")[0]);
        response.put("verificado", usuario.isVerificado());
        response.put("userId", usuario.getId());
        if (usuario.getCliente() != null) {
            response.put("clienteId", usuario.getCliente().getId());
        }
        return ResponseEntity.ok(response);
    }

    @PostMapping("/reenviar-codigo")
    public ResponseEntity<?> reenviarCodigo(@RequestBody Map<String, String> body) {
        String email = body.get("email");
        Usuario usuario = usuarioRepository.findByEmail(email).orElse(null);
        if (usuario == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "Usuario no encontrado"));
        }
        if (usuario.isVerificado()) {
            return ResponseEntity.badRequest().body(Map.of("error", "La cuenta ya está verificada"));
        }

        String codigo = String.format("%06d", new Random().nextInt(1000000));
        usuario.setTokenRegistro(passwordEncoder.encode(codigo));
        usuario.setTokenRegistroExpiracion(LocalDateTime.now().plusMinutes(3));
        usuarioRepository.save(usuario);

        emailService.enviarCodigoVerificacion(email, codigo);

        return ResponseEntity.ok(Map.of("mensaje", "Código reenviado al correo"));
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
