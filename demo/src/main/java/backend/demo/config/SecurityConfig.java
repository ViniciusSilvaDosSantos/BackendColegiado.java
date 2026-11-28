package backend.demo.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(Customizer.withDefaults())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        // Rotas públicas
                        .requestMatchers(HttpMethod.POST, "/api/auth/login").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/validar/**").permitAll()
                        .requestMatchers("/error").permitAll()

                        // Rotas exclusivas do Coordenador
                        .requestMatchers("/api/membros/**").hasRole("COORDENADOR")
                        .requestMatchers(HttpMethod.POST, "/api/reunioes").hasRole("COORDENADOR")
                        .requestMatchers(HttpMethod.POST, "/api/reunioes/*/pautas").hasRole("COORDENADOR")
                        .requestMatchers(HttpMethod.POST, "/api/reunioes/*/participacoes").hasRole("COORDENADOR")
                        .requestMatchers(HttpMethod.PUT, "/api/reunioes/*/finalizar").hasRole("COORDENADOR")
                        .requestMatchers(HttpMethod.POST, "/api/declaracoes").hasRole("COORDENADOR")

                        // Rotas liberadas para Coordenador e Membro
                        .requestMatchers(HttpMethod.POST, "/api/atas/*/assinar").hasAnyRole("COORDENADOR", "MEMBRO")
                        .requestMatchers(HttpMethod.GET, "/api/declaracoes/minha").hasAnyRole("COORDENADOR", "MEMBRO")
                        .requestMatchers(HttpMethod.GET, "/api/reunioes/**", "/api/atas/**").hasAnyRole("COORDENADOR", "MEMBRO")

                        // Qualquer outra rota exige autenticação
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOriginPatterns(List.of("*"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}