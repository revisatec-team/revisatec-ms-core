package cr.tec.revisatec.mscore.calendar.domain;

import java.util.Set;

public record CalendarEvent(Long id, String title, Long date, String audience, String type, Long courseId) {

    private static final Set<String> VALID_TYPES = Set.of("blue", "green", "amber");

    public CalendarEvent {
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("El título es obligatorio");
        }
        title = title.trim();
        if (date == null || date <= 0) {
            throw new IllegalArgumentException("date debe ser un Epoch en milisegundos mayor que 0");
        }
        if (audience == null || audience.isBlank()) {
            throw new IllegalArgumentException("La audiencia es obligatoria");
        }
        audience = audience.trim();
        if (type == null || !VALID_TYPES.contains(type)) {
            throw new IllegalArgumentException("type debe ser blue, green o amber");
        }
    }
}
