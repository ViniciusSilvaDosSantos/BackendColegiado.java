package backend.demo.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDateTime;
import java.util.Optional;

import backend.demo.entities.Participacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ParticipacaoRepository extends JpaRepository<Participacao, Long> {
    List<Participacao> findByReuniaoId(Long reuniaoId);

    Optional<Participacao> findByReuniaoIdAndMembroId(Long reuniaoId, Long membroId);

    @Query("select count(p) from Participacao p " +
           "where p.membro.id = :membroId and p.statusPresenca = 'PRESENTE' " +
           "and p.reuniao.status = 'FINALIZADA' " +
           "and p.reuniao.dataHora >= :inicio and p.reuniao.dataHora < :fim")
    long contarPresencas(@Param("membroId") Long membroId,
                         @Param("inicio") LocalDateTime inicio,
                         @Param("fim") LocalDateTime fim);
}
