package cr.tec.revisatec.mscore.group.application.service;

import cr.tec.revisatec.mscore.course.domain.PageResult;
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

    public GroupService(GroupRepositoryPort groupRepository) {
        this.groupRepository = groupRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public PageResult<Group> listGroups(int page, int pageSize, String search) {
        if (page < 1) {
            throw new IllegalArgumentException("page debe ser mayor o igual a 1");
        }
        if (pageSize < 1 || pageSize > MAX_PAGE_SIZE) {
            throw new IllegalArgumentException("pageSize debe estar entre 1 y 100");
        }
        return groupRepository.findAll(page, pageSize, search == null ? "" : search.trim());
    }

    @Override
    public Group createGroup(String name, String repoUrl, Integer avance, String estado) {
        return groupRepository.save(new Group(
                null,
                name,
                repoUrl,
                avance == null ? 0 : avance,
                estado == null ? "Pendiente" : estado));
    }

    @Override
    @Transactional(readOnly = true)
    public Group getGroup(Long id) {
        return findGroup(id);
    }

    @Override
    public Group updateGroup(Long id, String name, String repoUrl, Integer avance, String estado) {
        if (avance == null || estado == null) {
            throw new IllegalArgumentException("avance y estado son obligatorios");
        }
        Group existingGroup = findGroup(id);
        return groupRepository.save(new Group(existingGroup.id(), name, repoUrl, avance, estado));
    }

    @Override
    public void deleteGroup(Long id) {
        findGroup(id);
        groupRepository.deleteById(id);
    }

    private Group findGroup(Long id) {
        return groupRepository.findById(id).orElseThrow(() -> new GroupNotFoundException(id));
    }
}
