package cr.tec.revisatec.mscore.group.application.service;

import cr.tec.revisatec.mscore.course.domain.PageResult;
import cr.tec.revisatec.mscore.course.application.port.out.CourseRepositoryPort;
import cr.tec.revisatec.mscore.course.domain.CourseAssociationNotFoundException;
import cr.tec.revisatec.mscore.group.application.port.in.GroupUseCase;
import cr.tec.revisatec.mscore.group.application.port.out.GroupRepositoryPort;
import cr.tec.revisatec.mscore.group.domain.Group;
import cr.tec.revisatec.mscore.group.domain.GroupNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class GroupService implements GroupUseCase {

    private static final int MAX_PAGE_SIZE = 100;

    private final GroupRepositoryPort groupRepository;
    private final CourseRepositoryPort courseRepository;

    public GroupService(GroupRepositoryPort groupRepository, CourseRepositoryPort courseRepository) {
        this.groupRepository = groupRepository;
        this.courseRepository = courseRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public PageResult<Group> listGroups(int page, int pageSize, String search, Long courseId) {
        if (page < 1) {
            throw new IllegalArgumentException("page debe ser mayor o igual a 1");
        }
        if (pageSize < 1 || pageSize > MAX_PAGE_SIZE) {
            throw new IllegalArgumentException("pageSize debe estar entre 1 y 100");
        }
        return groupRepository.findAll(page, pageSize, search == null ? "" : search.trim(), courseId);
    }

    @Override
    public Group createGroup(String name, String repoUrl, Integer avance, String estado, Long courseId) {
        if (courseId == null) {
            throw new IllegalArgumentException("courseId es obligatorio");
        }
        validateCourse(courseId);
        return groupRepository.save(new Group(
                null,
                name,
                repoUrl,
                avance == null ? 0 : avance,
                estado == null ? "Pendiente" : estado,
                courseId));
    }

    @Override
    @Transactional(readOnly = true)
    public Group getGroup(Long id) {
        return findGroup(id);
    }

    @Override
    public Group updateGroup(Long id, String name, String repoUrl, Integer avance, String estado, Long courseId) {
        if (avance == null || estado == null) {
            throw new IllegalArgumentException("avance y estado son obligatorios");
        }
        Group existingGroup = findGroup(id);
        Long updatedCourseId = courseId == null ? existingGroup.courseId() : courseId;
        validateCourse(updatedCourseId);
        return groupRepository.save(new Group(existingGroup.id(), name, repoUrl, avance, estado, updatedCourseId));
    }

    @Override
    public void deleteGroup(Long id) {
        findGroup(id);
        groupRepository.deleteById(id);
    }

    private Group findGroup(Long id) {
        return groupRepository.findById(id).orElseThrow(() -> new GroupNotFoundException(id));
    }

    private void validateCourse(Long courseId) {
        if (courseId == null || courseRepository.findById(courseId).isEmpty()) {
            throw new CourseAssociationNotFoundException();
        }
    }
}
