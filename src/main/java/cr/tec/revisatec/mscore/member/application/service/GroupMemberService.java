package cr.tec.revisatec.mscore.member.application.service;

import cr.tec.revisatec.mscore.group.application.port.out.GroupRepositoryPort;
import cr.tec.revisatec.mscore.group.domain.Group;
import cr.tec.revisatec.mscore.group.domain.GroupNotFoundException;
import cr.tec.revisatec.mscore.member.application.port.in.GroupMemberUseCase;
import cr.tec.revisatec.mscore.member.application.port.out.GroupMemberRepositoryPort;
import cr.tec.revisatec.mscore.member.domain.DuplicateMemberException;
import cr.tec.revisatec.mscore.member.domain.GroupMember;
import cr.tec.revisatec.mscore.member.domain.MemberAssignment;
import cr.tec.revisatec.mscore.member.domain.MemberNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;

@Service
@Transactional
public class GroupMemberService implements GroupMemberUseCase {

    private final GroupMemberRepositoryPort memberRepository;
    private final GroupRepositoryPort groupRepository;

    public GroupMemberService(GroupMemberRepositoryPort memberRepository, GroupRepositoryPort groupRepository) {
        this.memberRepository = memberRepository;
        this.groupRepository = groupRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<GroupMember> listMembers(Long groupId) {
        findGroup(groupId);
        return memberRepository.findByGroupId(groupId);
    }

    @Override
    public GroupMember addMember(Long groupId, String name, String email, String githubUser) {
        Group group = findGroup(groupId);
        GroupMember candidate = new GroupMember(null, groupId, name, email, githubUser, System.currentTimeMillis());
        boolean alreadyInCourse = memberRepository.findByEmail(candidate.email()).stream()
                .map(existing -> groupRepository.findById(existing.groupId()))
                .flatMap(Optional::stream)
                .anyMatch(existingGroup -> Objects.equals(existingGroup.courseId(), group.courseId()));
        if (alreadyInCourse) {
            throw new DuplicateMemberException(candidate.email());
        }
        return memberRepository.save(candidate);
    }

    @Override
    public void removeMember(Long groupId, Long memberId) {
        findGroup(groupId);
        GroupMember member = memberRepository.findById(memberId)
                .filter(existing -> existing.groupId().equals(groupId))
                .orElseThrow(() -> new MemberNotFoundException(
                        "Integrante con id " + memberId + " no encontrado en el grupo " + groupId));
        memberRepository.deleteById(member.id());
    }

    @Override
    @Transactional(readOnly = true)
    public MemberAssignment findAssignment(String email, Long courseId) {
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("email es obligatorio");
        }
        if (courseId == null) {
            throw new IllegalArgumentException("courseId es obligatorio");
        }
        String normalizedEmail = email.trim().toLowerCase(Locale.ROOT);
        return memberRepository.findByEmail(normalizedEmail).stream()
                .flatMap(member -> groupRepository.findById(member.groupId())
                        .filter(group -> courseId.equals(group.courseId()))
                        .map(group -> new MemberAssignment(member, group))
                        .stream())
                .findFirst()
                .orElseThrow(() -> new MemberNotFoundException(
                        "El estudiante no está asignado a un grupo del curso " + courseId));
    }

    private Group findGroup(Long groupId) {
        return groupRepository.findById(groupId).orElseThrow(() -> new GroupNotFoundException(groupId));
    }
}
