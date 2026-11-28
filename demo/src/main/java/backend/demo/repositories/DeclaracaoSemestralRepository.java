package backend.demo.repositories;

import backend.demo.entities.DeclaracaoSemestral;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DeclaracaoSemestralRepository extends JpaRepository<DeclaracaoSemestral, Long> {
    Optional<DeclaracaoSemestral> findByCodigoAutenticacao(String codigoAutenticacao);
    boolean existsByMembroIdAndSemestre(Long membroId, String semestre);
    Optional<DeclaracaoSemestral> findFirstByMembroIdOrderByDataEmissaoDesc(Long membroId);
}