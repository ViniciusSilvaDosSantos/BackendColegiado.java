package backend.demo.repositories;

import backend.demo.entities.AssinaturaEletronica;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AssinaturaRepository extends JpaRepository<AssinaturaEletronica, Long> {
    boolean existsByParticipacaoId(Long participacaoId);
}
