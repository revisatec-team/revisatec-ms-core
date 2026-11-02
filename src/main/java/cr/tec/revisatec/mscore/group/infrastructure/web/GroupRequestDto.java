package cr.tec.revisatec.mscore.group.infrastructure.web;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record GroupRequestDto(
        @NotBlank(message = "El nombre del grupo no puede estar vacío") String name,
        @NotBlank(message = "La URL del repositorio es obligatoria") String repoUrl,
        @Min(value = 0, message = "avance debe estar entre 0 y 100")
        @Max(value = 100, message = "avance debe estar entre 0 y 100")
        Integer avance,
        @Pattern(regexp = "Pendiente|En progreso|Completado",
                message = "estado debe ser Pendiente, En progreso o Completado")
        String estado,
        Long courseId) {
}
