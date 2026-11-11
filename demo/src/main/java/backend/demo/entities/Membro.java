package backend.demo.entities;
import jakarta.persistence.*;
import lombok.Data;


@Data
@Entity
@Table(name = "membros")
public class Membro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false, unique = true, length = 50)
    private String matricula;

    @Column(nullable = false, length = 50)
    private String papel; // Ex: COORDENADOR, DOCENTE, DISCENTE

    @Column(nullable = false)
    private Boolean ativo = true;
}