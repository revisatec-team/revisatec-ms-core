package cr.tec.revisatec.mscore.member.application.port.in;

import cr.tec.revisatec.mscore.member.domain.GroupMember;
import cr.tec.revisatec.mscore.member.domain.MemberAssignment;

import java.util.List;

public interface GroupMemberUseCase {

    List<GroupMember> listMembers(Long groupId);

    GroupMember addMember(Long groupId, String name, String email, String githubUser);

    void removeMember(Long groupId, Long memberId);

    MemberAssignment findAssignment(String email, Long courseId);
}
