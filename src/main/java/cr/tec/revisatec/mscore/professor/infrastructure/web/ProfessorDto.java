package cr.tec.revisatec.mscore.professor.infrastructure.web;

import cr.tec.revisatec.mscore.professor.domain.Professor;

import java.util.List;

public record ProfessorDto(Long id, String name, String email, List<Long> courseIds) {

    public static ProfessorDto from(Professor professor) {
        return new ProfessorDto(
                professor.id(), professor.name(), professor.email(),
                professor.courseIds().stream().sorted().toList());
    }
}
