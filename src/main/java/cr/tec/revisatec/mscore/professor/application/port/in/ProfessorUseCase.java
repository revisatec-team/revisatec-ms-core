package cr.tec.revisatec.mscore.professor.application.port.in;

import cr.tec.revisatec.mscore.professor.domain.Professor;

import java.util.List;
import java.util.Set;

public interface ProfessorUseCase {

    List<Professor> listProfessors();

    Professor createProfessor(String name, String email, Set<Long> courseIds);

    void deleteProfessor(Long id);
}
