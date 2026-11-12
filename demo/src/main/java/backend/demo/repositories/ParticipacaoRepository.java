package backend.demo.repositories;

import backend.demo.entities.Participacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ParticipacaoRepository extends JpaRepository<Participacao, Long> {
    List<Participacao> findByReuniaoId(Long reuniaoId);
}