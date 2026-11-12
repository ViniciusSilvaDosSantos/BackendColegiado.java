package backend.demo;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DemoApplication {

    public static void main(String[] args) {
        // Ensinando ao Java que o arquivo está dentro da pasta "demo"
        Dotenv dotenv = Dotenv.configure().directory("./demo").ignoreIfMissing().load();

        // Passa as variáveis para o sistema
        dotenv.entries().forEach(entry -> System.setProperty(entry.getKey(), entry.getValue()));

        SpringApplication.run(DemoApplication.class, args);
    }
}