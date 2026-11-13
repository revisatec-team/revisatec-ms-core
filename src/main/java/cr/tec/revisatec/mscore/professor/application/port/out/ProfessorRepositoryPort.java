package cr.tec.revisatec.mscore.professor.application.port.out;

import cr.tec.revisatec.mscore.professor.domain.Professor;

import java.util.List;
import java.util.Optional;

public interface ProfessorRepositoryPort {

    List<Professor> findAll();

    Optional<Professor> findById(Long id);

    Optional<Professor> findByEmail(String email);

    Professor save(Professor professor);

    void deleteById(Long id);
}
