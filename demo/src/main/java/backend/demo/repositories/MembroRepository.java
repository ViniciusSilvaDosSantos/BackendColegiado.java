package backend.demo.repositories;

import backend.demo.entities.Membro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

// Se o Integrante 2 já criou o dele, use o dele e apague este.
@Repository
public interface MembroRepository extends JpaRepository<Membro, Long> {
    Optional<Membro> findByEmail(String email);
    boolean existsByEmail(String email);
}
