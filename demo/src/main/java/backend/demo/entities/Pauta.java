package backend.demo.entities;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "pautas")
public class Pauta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reuniao_id", nullable = false)
    private Reuniao reuniao;

    @Column(nullable = false)
    private String titulo;

    @Column(columnDefinition = "TEXT")
    private String descricao;

    @Column(name = "ordem_apresentacao", nullable = false)
    private Integer ordemApresentacao;

    @Column(columnDefinition = "TEXT")
    private String deliberacao;
}
