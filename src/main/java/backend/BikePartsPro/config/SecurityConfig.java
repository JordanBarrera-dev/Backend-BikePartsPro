package backend.BikePartsPro.config;

import backend.BikePartsPro.security.JwtFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

// @EnableWebSecurity: activa el módulo de seguridad web de Spring Security.
// Esta clase define el SecurityFilterChain: las reglas que determinan
// qué solicitudes pasan y cuáles se bloquean, y con qué condiciones.
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtFilter jwtFilter;
    private final AuthenticationProvider authenticationProvider;

    public SecurityConfig(JwtFilter jwtFilter,
                          AuthenticationProvider authenticationProvider) {
        this.jwtFilter = jwtFilter;
        this.authenticationProvider = authenticationProvider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
    .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.configure(http))
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                .authorizeHttpRequests(auth -> auth

                        // Swagger: público
                        .requestMatchers(
                                "/swagger-ui/**",
                                "/v3/api-docs/**",
                                "/swagger-ui.html"
                        ).permitAll()

                        // Auth público — DEBE ir antes que cualquier regla específica de /auth/**
                        .requestMatchers("/auth/register", "/auth/login").permitAll()

                        // Promover: solo ADMIN
                        .requestMatchers(HttpMethod.PUT, "/auth/promover/**").hasRole("ADMIN")

                        // GET: CLIENTE y ADMIN
                        .requestMatchers(HttpMethod.GET, "/productos/**").hasAnyRole("ADMIN", "CLIENTE")
                        .requestMatchers(HttpMethod.GET, "/clientes/**").hasAnyRole("ADMIN", "CLIENTE")
                        .requestMatchers(HttpMethod.GET, "/ordenes/**").hasAnyRole("ADMIN", "CLIENTE")

                        // Solo ADMIN
                        .requestMatchers(HttpMethod.POST, "/productos/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/productos/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/productos/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/clientes/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/clientes/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/clientes/**").hasRole("ADMIN")

                        // Órdenes
                        .requestMatchers(HttpMethod.POST, "/ordenes/**").hasAnyRole("ADMIN", "CLIENTE")

                        .anyRequest().authenticated()
                )

                // Registrar el AuthenticationProvider que usa la BD.
                .authenticationProvider(authenticationProvider)

                // Insertar JwtFilter antes del filtro estándar de usuario/contraseña.
                // Así, cada solicitud primero pasa por la validación del token JWT.
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}