package backend.BikePartsPro.security;

import backend.BikePartsPro.model.Usuario;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtil {

    // @Value lee el valor desde application.properties.
    // Spring lo inyecta automáticamente al crear el bean.
    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private long expiration;

    // generateToken: recibe un UserDetails (el Usuario autenticado)
    // y genera un JWT firmado con los datos del usuario.
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("rol", userDetails.getAuthorities().iterator().next().getAuthority());

        if (userDetails instanceof Usuario usuario) {
            String[] partes = usuario.getNombre().trim().split("\\s+");
            claims.put("nombre", partes[0]);
        }

        return Jwts.builder()
                .claims(claims)
                .subject(userDetails.getUsername())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSigningKey())
                .compact();
    }

    public String extractEmail(String token) {
        return extractClaims(token).getSubject();
    }

    public String extractNombre(String token) {
        Object nombre = extractClaims(token).get("nombre");
        return nombre != null ? nombre.toString() : null;
    }

    // isTokenValid: verifica dos cosas:
    // 1. El email del token coincide con el usuario cargado.
    // 2. El token no ha vencido.
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String email = extractEmail(token);
        return email.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    // isTokenExpired: compara la fecha de vencimiento con la fecha actual.
    private boolean isTokenExpired(String token) {
        return extractClaims(token).getExpiration().before(new Date());
    }

    // extractClaims: parsea el token, verifica la firma y retorna el payload.
    // Si la firma no es válida o el token fue alterado, JJWT lanza una excepción.
    private Claims extractClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    // getSigningKey: convierte la clave hexadecimal del properties
    // en un objeto SecretKey que JJWT puede usar para firmar y verificar.
    private SecretKey getSigningKey() {
        byte[] keyBytes = hexToBytes(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    // hexToBytes: convierte la representación hexadecimal de la clave
    // a un arreglo de bytes. Necesario porque JWT trabaja con bytes, no strings.
    private byte[] hexToBytes(String hex) {
        int length = hex.length();
        byte[] bytes = new byte[length / 2];
        for (int i = 0; i < length; i += 2) {
            bytes[i / 2] = (byte) ((Character.digit(hex.charAt(i), 16) << 4)
                    + Character.digit(hex.charAt(i + 1), 16));
        }
        return bytes;
    }
}
