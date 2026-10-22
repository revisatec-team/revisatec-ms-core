package cr.tec.revisatec.mscore.group.infrastructure.web;

import cr.tec.revisatec.mscore.course.domain.PageResult;
import cr.tec.revisatec.mscore.group.application.port.in.GroupUseCase;
import cr.tec.revisatec.mscore.group.domain.Group;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/groups")
public class GroupController {

    private final GroupUseCase groupUseCase;

    public GroupController(GroupUseCase groupUseCase) {
        this.groupUseCase = groupUseCase;
    }

    @GetMapping
    public PageResult<GroupDto> listGroups(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(defaultValue = "") String search) {
        PageResult<Group> result = groupUseCase.listGroups(page, pageSize, search);
        List<GroupDto> items = result.items().stream().map(GroupDto::from).toList();
        return new PageResult<>(result.totalRecords(), result.page(), result.pageSize(), items);
    }

    @PostMapping
    public ResponseEntity<GroupDto> createGroup(@Valid @RequestBody GroupRequestDto request) {
        Group group = groupUseCase.createGroup(
                request.name(), request.repoUrl(), request.avance(), request.estado());
        return ResponseEntity.status(HttpStatus.CREATED).body(GroupDto.from(group));
    }

    @GetMapping("/{id}")
    public GroupDto getGroup(@PathVariable Long id) {
        return GroupDto.from(groupUseCase.getGroup(id));
    }

    @PutMapping("/{id}")
    public GroupDto updateGroup(@PathVariable Long id, @Valid @RequestBody GroupRequestDto request) {
        return GroupDto.from(groupUseCase.updateGroup(
                id, request.name(), request.repoUrl(), request.avance(), request.estado()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGroup(@PathVariable Long id) {
        groupUseCase.deleteGroup(id);
        return ResponseEntity.ok().build();
    }
}
