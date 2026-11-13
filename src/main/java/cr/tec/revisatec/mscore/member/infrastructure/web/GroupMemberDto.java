package cr.tec.revisatec.mscore.member.infrastructure.web;

import cr.tec.revisatec.mscore.member.domain.GroupMember;

public record GroupMemberDto(Long id, Long groupId, String name, String email, String githubUser, long createdAt) {

    public static GroupMemberDto from(GroupMember member) {
        return new GroupMemberDto(
                member.id(), member.groupId(), member.name(), member.email(),
                member.githubUser(), member.createdAt());
    }
}
