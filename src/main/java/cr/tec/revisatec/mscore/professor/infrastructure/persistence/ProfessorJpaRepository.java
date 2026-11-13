package cr.tec.revisatec.mscore.professor.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProfessorJpaRepository extends JpaRepository<ProfessorEntity, Long> {

    Optional<ProfessorEntity> findByEmail(String email);

    List<ProfessorEntity> findAllByOrderByNameAsc();
}
