package backend.demo;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DemoApplication {

    public static void main(String[] args) {
        // Procura o .env na pasta onde a app roda (a pasta demo/ no IntelliJ e no ./mvnw)
        // e não quebra se o arquivo não existir (variáveis do sistema continuam valendo).
        Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();
        dotenv.entries().forEach(e -> System.setProperty(e.getKey(), e.getValue()));

        SpringApplication.run(DemoApplication.class, args);
    }
}
