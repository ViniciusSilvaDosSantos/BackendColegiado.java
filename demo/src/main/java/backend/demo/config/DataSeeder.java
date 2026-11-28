package backend.demo.config;

import backend.demo.entities.Membro;
import backend.demo.entities.Perfil;
import backend.demo.repositories.MembroRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@ConditionalOnProperty(name = "app.seed.enabled", havingValue = "true")
public class DataSeeder implements CommandLineRunner {

    private final MembroRepository membroRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.seed.admin-email}")
    private String adminEmail;

    @Value("${app.seed.admin-password}")
    private String adminPassword;

    @Override
    public void run(String... args) {
        if (!membroRepository.existsByEmail(adminEmail)) {
            membroRepository.save(Membro.builder()
                    .nome("Coordenador Inicial")
                    .email(adminEmail)
                    .senha(passwordEncoder.encode(adminPassword))
                    .perfil(Perfil.COORDENADOR)
                    .ativo(true)
                    .build());
        }
    }
}