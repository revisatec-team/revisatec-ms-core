package cr.tec.revisatec.mscore.member.infrastructure.web;

import cr.tec.revisatec.mscore.member.application.port.in.GroupMemberUseCase;
import cr.tec.revisatec.mscore.member.domain.GroupMember;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class GroupMemberController {

    private final GroupMemberUseCase memberUseCase;

    public GroupMemberController(GroupMemberUseCase memberUseCase) {
        this.memberUseCase = memberUseCase;
    }

    @GetMapping("/groups/{groupId}/members")
    public GroupMembersResponse listMembers(@PathVariable Long groupId) {
        List<GroupMemberDto> members = memberUseCase.listMembers(groupId).stream()
                .map(GroupMemberDto::from).toList();
        return new GroupMembersResponse(groupId, members.size(), members);
    }

    @PostMapping("/groups/{groupId}/members")
    public ResponseEntity<GroupMemberDto> addMember(
            @PathVariable Long groupId, @Valid @RequestBody GroupMemberRequestDto request) {
        GroupMember member = memberUseCase.addMember(groupId, request.name(), request.email(), request.githubUser());
        return ResponseEntity.status(HttpStatus.CREATED).body(GroupMemberDto.from(member));
    }

    @DeleteMapping("/groups/{groupId}/members/{memberId}")
    public ResponseEntity<Void> removeMember(@PathVariable Long groupId, @PathVariable Long memberId) {
        memberUseCase.removeMember(groupId, memberId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/members/me")
    public MemberAssignmentDto findMyAssignment(@RequestParam String email, @RequestParam Long courseId) {
        return MemberAssignmentDto.from(memberUseCase.findAssignment(email, courseId));
    }
}
