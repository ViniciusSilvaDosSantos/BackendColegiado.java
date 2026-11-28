package backend.demo;

import backend.demo.entities.Membro;
import backend.demo.entities.Perfil;
import backend.demo.repositories.MembroRepository;
import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.nio.file.Files;
import java.nio.file.Path;

@SpringBootApplication
public class DemoApplication {

    public static void main(String[] args) {
        String envDir = Files.exists(Path.of("demo/.env")) ? "./demo" : ".";
        Dotenv dotenv = Dotenv.configure()
                .directory(envDir)
                .ignoreIfMissing()
                .load();
        dotenv.entries().forEach(entry -> System.setProperty(entry.getKey(), entry.getValue()));

        SpringApplication.run(DemoApplication.class, args);
    }

    @Bean
    CommandLineRunner seedCoordenador(MembroRepository repo, PasswordEncoder encoder) {
        return args -> {
            if (!repo.existsByEmail("coord@ifpa.edu.br")) {
                repo.save(Membro.builder()
                        .nome("Coordenador Teste")
                        .email("coord@ifpa.edu.br")
                        .senha(encoder.encode("123456"))
                        .perfil(Perfil.COORDENADOR)
                        .ativo(true)
                        .build());
            }
        };
    }
}