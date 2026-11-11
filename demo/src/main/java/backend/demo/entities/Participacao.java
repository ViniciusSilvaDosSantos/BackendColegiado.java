package backend.demo.entities;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "participacoes", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"reuniao_id", "membro_id"})
})
public class Participacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reuniao_id", nullable = false)
    private Reuniao reuniao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "membro_id", nullable = false)
    private Membro membro;

    @Column(name = "status_presenca", length = 30)
    private String statusPresenca = "PENDENTE"; // PRESENTE, AUSENTE, JUSTIFICADO

    @Column(name = "justificativa_falta", columnDefinition = "TEXT")
    private String justificativaFalta;
}