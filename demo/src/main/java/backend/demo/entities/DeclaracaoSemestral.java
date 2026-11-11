package backend.demo.entities;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "declaracoes_semestrais", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"membro_id", "semestre"})
})
public class DeclaracaoSemestral {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "membro_id", nullable = false)
    private Membro membro;

    @Column(nullable = false, length = 10)
    private String semestre;

    @Column(name = "codigo_autenticacao", nullable = false, unique = true, length = 100)
    private String codigoAutenticacao;

    @Column(name = "quantidade_reunioes_participadas", nullable = false)
    private Integer quantidadeReunioesParticipadas;

    @Column(name = "carga_horaria_total", nullable = false)
    private Integer cargaHorariaTotal;

    @Column(name = "data_emissao", nullable = false)
    private LocalDateTime dataEmissao = LocalDateTime.now();
}
