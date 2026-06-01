package backend.BikePartsPro.config;

import backend.BikePartsPro.security.JwtFilter;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfigurationSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtFilter jwtFilter;
    private final AuthenticationProvider authenticationProvider;
    private final CorsConfigurationSource corsConfigurationSource;

    public SecurityConfig(JwtFilter jwtFilter,
                          AuthenticationProvider authenticationProvider,
                          CorsConfigurationSource corsConfigurationSource) {
        this.jwtFilter = jwtFilter;
        this.authenticationProvider = authenticationProvider;
        this.corsConfigurationSource = corsConfigurationSource;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
            .csrf(csrf -> csrf.disable())
            .cors(cors -> cors.configurationSource(corsConfigurationSource))
            .sessionManagement(session ->
                    session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

            .authorizeHttpRequests(auth -> auth

                    .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                    .requestMatchers(
                            "/swagger-ui/**",
                            "/v3/api-docs/**",
                            "/swagger-ui.html"
                    ).permitAll()

                    .requestMatchers(HttpMethod.POST,
                            "/auth/register",
                            "/auth/login",
                            "/auth/verificar-registro",
                            "/auth/reenviar-codigo"
                    ).permitAll()

                    .requestMatchers(HttpMethod.PUT, "/auth/promover/**").hasRole("ADMIN")

                    .requestMatchers(HttpMethod.GET, "/productos/**").permitAll()
                    .requestMatchers(HttpMethod.GET, "/marcas/**").permitAll()
                    .requestMatchers(HttpMethod.POST, "/marcas/**").hasRole("ADMIN")
                    .requestMatchers(HttpMethod.DELETE, "/marcas/**").hasRole("ADMIN")
                    .requestMatchers(HttpMethod.GET, "/modelos-producto/**").permitAll()
                    .requestMatchers(HttpMethod.POST, "/modelos-producto/**").hasRole("ADMIN")
                    .requestMatchers(HttpMethod.DELETE, "/modelos-producto/**").hasRole("ADMIN")
                    .requestMatchers(HttpMethod.GET, "/clientes/**").hasAnyRole("ADMIN", "CLIENTE")
                    .requestMatchers(HttpMethod.GET, "/ordenes/**").hasAnyRole("ADMIN", "CLIENTE")

                    .requestMatchers(HttpMethod.POST, "/productos/**").hasRole("ADMIN")
                    .requestMatchers(HttpMethod.PUT, "/productos/**").hasRole("ADMIN")
                    .requestMatchers(HttpMethod.DELETE, "/productos/**").hasRole("ADMIN")
                    .requestMatchers(HttpMethod.POST, "/clientes/**").hasRole("ADMIN")
                    .requestMatchers(HttpMethod.PUT, "/clientes/**").hasRole("ADMIN")
                    .requestMatchers(HttpMethod.DELETE, "/clientes/**").hasRole("ADMIN")

                    .requestMatchers(HttpMethod.POST, "/ordenes/**").hasAnyRole("ADMIN", "CLIENTE")
                    .requestMatchers(HttpMethod.PUT, "/ordenes/**").hasAnyRole("ADMIN", "CLIENTE")
                    .requestMatchers(HttpMethod.DELETE, "/ordenes/**").hasRole("ADMIN")

                    .requestMatchers(HttpMethod.GET, "/envios/**").hasAnyRole("ADMIN", "CLIENTE")
                    .requestMatchers(HttpMethod.POST, "/envios/**").hasAnyRole("ADMIN", "CLIENTE")
                    .requestMatchers(HttpMethod.PUT, "/envios/**").hasAnyRole("ADMIN", "CLIENTE")
                    .requestMatchers(HttpMethod.DELETE, "/envios/**").hasRole("ADMIN")

                    .requestMatchers(HttpMethod.GET, "/departamentos/**").permitAll()
                    .requestMatchers(HttpMethod.POST, "/departamentos/**").hasRole("ADMIN")
                    .requestMatchers(HttpMethod.PUT, "/departamentos/**").hasRole("ADMIN")
                    .requestMatchers(HttpMethod.DELETE, "/departamentos/**").hasRole("ADMIN")

                    .requestMatchers(HttpMethod.GET, "/ciudades/**").permitAll()
                    .requestMatchers(HttpMethod.POST, "/ciudades/**").hasRole("ADMIN")
                    .requestMatchers(HttpMethod.PUT, "/ciudades/**").hasRole("ADMIN")
                    .requestMatchers(HttpMethod.DELETE, "/ciudades/**").hasRole("ADMIN")

                    .anyRequest().authenticated()
            )

            .exceptionHandling(ex -> ex
                    .authenticationEntryPoint((req, res, e) ->
                            res.sendError(HttpServletResponse.SC_UNAUTHORIZED, "No autenticado"))
                    .accessDeniedHandler((req, res, e) ->
                            res.sendError(HttpServletResponse.SC_FORBIDDEN, "Acceso denegado"))
            )
            .authenticationProvider(authenticationProvider)
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
