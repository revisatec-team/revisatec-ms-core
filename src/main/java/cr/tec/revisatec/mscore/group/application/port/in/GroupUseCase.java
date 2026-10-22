package cr.tec.revisatec.mscore.group.application.port.in;

import cr.tec.revisatec.mscore.course.domain.PageResult;
import cr.tec.revisatec.mscore.group.domain.Group;

public interface GroupUseCase {

    PageResult<Group> listGroups(int page, int pageSize, String search);

    Group createGroup(String name, String repoUrl, Integer avance, String estado);

    Group getGroup(Long id);

    Group updateGroup(Long id, String name, String repoUrl, Integer avance, String estado);

    void deleteGroup(Long id);
}
