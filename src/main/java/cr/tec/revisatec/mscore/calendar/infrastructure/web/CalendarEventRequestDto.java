package cr.tec.revisatec.mscore.calendar.infrastructure.web;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;

public record CalendarEventRequestDto(
        @NotBlank(message = "El título es obligatorio") String title,
        @Positive(message = "date debe ser un Epoch en milisegundos mayor que 0") Long date,
        @NotBlank(message = "La audiencia es obligatoria") String audience,
        @Pattern(regexp = "blue|green|amber", message = "type debe ser blue, green o amber")
        String type,
        Long courseId) {
}
