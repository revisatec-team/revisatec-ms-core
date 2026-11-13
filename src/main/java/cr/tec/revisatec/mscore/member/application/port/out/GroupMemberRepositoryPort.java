package cr.tec.revisatec.mscore.member.application.port.out;

import cr.tec.revisatec.mscore.member.domain.GroupMember;

import java.util.List;
import java.util.Optional;

public interface GroupMemberRepositoryPort {

    List<GroupMember> findByGroupId(Long groupId);

    List<GroupMember> findByEmail(String email);

    Optional<GroupMember> findById(Long id);

    GroupMember save(GroupMember member);

    void deleteById(Long id);

    void deleteByGroupId(Long groupId);
}
