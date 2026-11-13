package cr.tec.revisatec.mscore.identity.infrastructure.web;

import cr.tec.revisatec.mscore.course.infrastructure.web.CourseDto;
import cr.tec.revisatec.mscore.group.infrastructure.web.GroupDto;
import cr.tec.revisatec.mscore.identity.domain.Identity;
import cr.tec.revisatec.mscore.member.infrastructure.web.GroupMemberDto;

import java.util.List;
import java.util.Locale;

public record IdentityDto(String role, String name, String email, List<CourseDto> courses, List<MembershipDto> memberships) {

    public static IdentityDto from(Identity identity) {
        return new IdentityDto(
                identity.role().name().toLowerCase(Locale.ROOT),
                identity.name(),
                identity.email(),
                identity.courses().stream().map(CourseDto::from).toList(),
                identity.memberships().stream()
                        .map(membership -> new MembershipDto(
                                CourseDto.from(membership.course()),
                                GroupDto.from(membership.group()),
                                GroupMemberDto.from(membership.member())))
                        .toList());
    }

    public record MembershipDto(CourseDto course, GroupDto group, GroupMemberDto member) {
    }
}
