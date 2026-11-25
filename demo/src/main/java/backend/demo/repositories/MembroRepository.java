package backend.demo.repositories;

import backend.demo.entities.Membro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MembroRepository extends JpaRepository<Membro, Long> {

    Optional<Membro> findByEmail(String email);

    boolean existsByEmail(String email);

    List<Membro> findByAtivoTrue();
}