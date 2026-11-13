package cr.tec.revisatec.mscore.member.infrastructure.web;

import cr.tec.revisatec.mscore.group.infrastructure.web.GroupDto;
import cr.tec.revisatec.mscore.member.domain.MemberAssignment;

public record MemberAssignmentDto(GroupMemberDto member, GroupDto group) {

    public static MemberAssignmentDto from(MemberAssignment assignment) {
        return new MemberAssignmentDto(
                GroupMemberDto.from(assignment.member()), GroupDto.from(assignment.group()));
    }
}
