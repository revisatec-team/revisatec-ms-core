package cr.tec.revisatec.mscore.professor.infrastructure.web;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.Set;

public record ProfessorRequestDto(
        @NotBlank(message = "El nombre del profesor no puede estar vacío") String name,
        @NotBlank(message = "El correo del profesor es obligatorio")
        @Email(message = "El correo del profesor no es válido") String email,
        @NotEmpty(message = "El profesor debe tener al menos un curso asignado") Set<Long> courseIds) {
}
