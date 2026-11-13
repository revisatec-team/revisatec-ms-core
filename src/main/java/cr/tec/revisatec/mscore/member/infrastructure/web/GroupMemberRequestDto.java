package cr.tec.revisatec.mscore.member.infrastructure.web;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record GroupMemberRequestDto(
        @NotBlank(message = "El nombre del integrante no puede estar vacío") String name,
        @NotBlank(message = "El correo del integrante es obligatorio")
        @Email(message = "El correo del integrante no es válido") String email,
        String githubUser) {
}
