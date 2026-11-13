package cr.tec.revisatec.mscore.member.infrastructure.web;

import java.util.List;

public record GroupMembersResponse(Long groupId, int totalRecords, List<GroupMemberDto> members) {
}
