package backend.demo.entities;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "assinaturas_eletronicas")
public class AssinaturaEletronica {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "participacao_id", nullable = false, unique = true)
    private Participacao participacao;

    @Column(name = "hash_assinatura", nullable = false)
    private String hashAssinatura;

    @Column(name = "ip_origem", length = 45)
    private String ipOrigem;

    @Column(name = "data_hora_assinatura", nullable = false)
    private LocalDateTime dataHoraAssinatura = LocalDateTime.now();
}
