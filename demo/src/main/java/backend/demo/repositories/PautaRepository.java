package backend.demo.repositories;

import backend.demo.entities.Pauta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PautaRepository extends JpaRepository<Pauta, Long> {
    // O Spring Boot cria a query (SELECT * FROM pautas WHERE reuniao_id = ? ORDER BY ordem_apresentacao ASC)
    List<Pauta> findByReuniaoIdOrderByOrdemApresentacaoAsc(Long reuniaoId);
}