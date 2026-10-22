package cr.tec.revisatec.mscore.group.application.port.out;

import cr.tec.revisatec.mscore.course.domain.PageResult;
import cr.tec.revisatec.mscore.group.domain.Group;

import java.util.Optional;

public interface GroupRepositoryPort {

    PageResult<Group> findAll(int page, int pageSize, String search);

    Optional<Group> findById(Long id);

    Group save(Group group);

    void deleteById(Long id);
}
