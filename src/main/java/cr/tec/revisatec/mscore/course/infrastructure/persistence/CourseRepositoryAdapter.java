package cr.tec.revisatec.mscore.course.infrastructure.persistence;

import cr.tec.revisatec.mscore.course.application.port.out.CourseRepositoryPort;
import cr.tec.revisatec.mscore.course.domain.Course;
import cr.tec.revisatec.mscore.course.domain.PageResult;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class CourseRepositoryAdapter implements CourseRepositoryPort {

    private final CourseJpaRepository repository;

    public CourseRepositoryAdapter(CourseJpaRepository repository) {
        this.repository = repository;
    }

    @Override
    public PageResult<Course> findAll(int page, int pageSize, String search) {
        PageRequest pageable = PageRequest.of(page - 1, pageSize);
        Page<CourseEntity> result = search.isBlank()
                ? repository.findAll(pageable)
                : repository.findByNameContainingIgnoreCase(search, pageable);
        return new PageResult<>(
                result.getTotalElements(),
                page,
                pageSize,
                result.getContent().stream().map(CourseRepositoryAdapter::toDomain).toList());
    }

    @Override
    public Optional<Course> findById(Long id) {
        return repository.findById(id).map(CourseRepositoryAdapter::toDomain);
    }

    @Override
    public Course save(Course course) {
        CourseEntity entity = repository.save(new CourseEntity(course.id(), course.name()));
        return toDomain(entity);
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    private static Course toDomain(CourseEntity entity) {
        return new Course(entity.getId(), entity.getName());
    }
}
