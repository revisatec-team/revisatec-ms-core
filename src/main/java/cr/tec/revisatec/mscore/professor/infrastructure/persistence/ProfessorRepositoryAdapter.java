package cr.tec.revisatec.mscore.professor.infrastructure.persistence;

import cr.tec.revisatec.mscore.professor.application.port.out.ProfessorRepositoryPort;
import cr.tec.revisatec.mscore.professor.domain.Professor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class ProfessorRepositoryAdapter implements ProfessorRepositoryPort {

    private final ProfessorJpaRepository repository;

    public ProfessorRepositoryAdapter(ProfessorJpaRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Professor> findAll() {
        return repository.findAllByOrderByNameAsc().stream().map(ProfessorRepositoryAdapter::toDomain).toList();
    }

    @Override
    public Optional<Professor> findById(Long id) {
        return repository.findById(id).map(ProfessorRepositoryAdapter::toDomain);
    }

    @Override
    public Optional<Professor> findByEmail(String email) {
        return repository.findByEmail(email).map(ProfessorRepositoryAdapter::toDomain);
    }

    @Override
    public Professor save(Professor professor) {
        return toDomain(repository.save(new ProfessorEntity(
                professor.id(), professor.name(), professor.email(), professor.courseIds())));
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    private static Professor toDomain(ProfessorEntity entity) {
        return new Professor(entity.getId(), entity.getName(), entity.getEmail(), entity.getCourseIds());
    }
}
