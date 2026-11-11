package backend.demo.entities;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "reunioes")
public class Reuniao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "data_hora", nullable = false)
    private LocalDateTime dataHora;

    @Column(nullable = false)
    private String local;

    @Column(length = 50)
    private String status = "AGENDADA"; // AGENDADA, EM_ANDAMENTO, FINALIZADA

    @Column(name = "ata_conteudo", columnDefinition = "TEXT")
    private String ataConteudo;

    @Column(name = "data_geracao_ata")
    private LocalDateTime dataGeracaoAta;
}