package cr.tec.revisatec.mscore.professor.application.service;

import cr.tec.revisatec.mscore.course.application.port.out.CourseRepositoryPort;
import cr.tec.revisatec.mscore.course.domain.CourseAssociationNotFoundException;
import cr.tec.revisatec.mscore.professor.application.port.in.ProfessorUseCase;
import cr.tec.revisatec.mscore.professor.application.port.out.ProfessorRepositoryPort;
import cr.tec.revisatec.mscore.professor.domain.DuplicateProfessorException;
import cr.tec.revisatec.mscore.professor.domain.Professor;
import cr.tec.revisatec.mscore.professor.domain.ProfessorNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Service
@Transactional
public class ProfessorService implements ProfessorUseCase {

    private final ProfessorRepositoryPort professorRepository;
    private final CourseRepositoryPort courseRepository;

    public ProfessorService(ProfessorRepositoryPort professorRepository, CourseRepositoryPort courseRepository) {
        this.professorRepository = professorRepository;
        this.courseRepository = courseRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Professor> listProfessors() {
        return professorRepository.findAll();
    }

    @Override
    public Professor createProfessor(String name, String email, Set<Long> courseIds) {
        Professor candidate = new Professor(null, name, email, courseIds);
        if (professorRepository.findByEmail(candidate.email()).isPresent()) {
            throw new DuplicateProfessorException(candidate.email());
        }
        for (Long courseId : candidate.courseIds()) {
            if (courseRepository.findById(courseId).isEmpty()) {
                throw new CourseAssociationNotFoundException();
            }
        }
        return professorRepository.save(candidate);
    }

    @Override
    public void deleteProfessor(Long id) {
        professorRepository.findById(id).orElseThrow(() -> new ProfessorNotFoundException(id));
        professorRepository.deleteById(id);
    }
}
