package cr.tec.revisatec.mscore.professor.infrastructure.web;

import cr.tec.revisatec.mscore.professor.application.port.in.ProfessorUseCase;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/professors")
public class ProfessorController {

    private final ProfessorUseCase professorUseCase;

    public ProfessorController(ProfessorUseCase professorUseCase) {
        this.professorUseCase = professorUseCase;
    }

    @GetMapping
    public ProfessorListResponse listProfessors() {
        List<ProfessorDto> items = professorUseCase.listProfessors().stream().map(ProfessorDto::from).toList();
        return new ProfessorListResponse(items.size(), items);
    }

    @PostMapping
    public ResponseEntity<ProfessorDto> createProfessor(@Valid @RequestBody ProfessorRequestDto request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ProfessorDto.from(
                professorUseCase.createProfessor(request.name(), request.email(), request.courseIds())));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProfessor(@PathVariable Long id) {
        professorUseCase.deleteProfessor(id);
        return ResponseEntity.ok().build();
    }

    public record ProfessorListResponse(int totalRecords, List<ProfessorDto> items) {
    }
}
