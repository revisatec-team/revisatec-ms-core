package cr.tec.revisatec.mscore.group.infrastructure.persistence;

import cr.tec.revisatec.mscore.course.domain.PageResult;
import cr.tec.revisatec.mscore.group.application.port.out.GroupRepositoryPort;
import cr.tec.revisatec.mscore.group.domain.Group;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class GroupRepositoryAdapter implements GroupRepositoryPort {

    private final GroupJpaRepository repository;

    public GroupRepositoryAdapter(GroupJpaRepository repository) {
        this.repository = repository;
    }

    @Override
    public PageResult<Group> findAll(int page, int pageSize, String search, Long courseId) {
        PageRequest pageable = PageRequest.of(page - 1, pageSize);
        Page<GroupEntity> result;
        if (courseId == null) {
            result = search.isBlank()
                    ? repository.findAll(pageable)
                    : repository.findByNameContainingIgnoreCase(search, pageable);
        } else {
            result = search.isBlank()
                    ? repository.findByCourseId(courseId, pageable)
                    : repository.findByNameContainingIgnoreCaseAndCourseId(search, courseId, pageable);
        }
        return new PageResult<>(
                result.getTotalElements(),
                page,
                pageSize,
                result.getContent().stream().map(GroupRepositoryAdapter::toDomain).toList());
    }

    @Override
    public Optional<Group> findById(Long id) {
        return repository.findById(id).map(GroupRepositoryAdapter::toDomain);
    }

    @Override
    public Group save(Group group) {
        GroupEntity entity = repository.save(new GroupEntity(
                group.id(), group.name(), group.repoUrl(), group.avance(), group.estado(), group.courseId()));
        return toDomain(entity);
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    private static Group toDomain(GroupEntity entity) {
        return new Group(
                entity.getId(), entity.getName(), entity.getRepoUrl(),
                entity.getAvance(), entity.getEstado(), entity.getCourseId());
    }
}
