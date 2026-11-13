package cr.tec.revisatec.mscore.professor.domain;

import java.util.Locale;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Pattern;

public record Professor(Long id, String name, String email, Set<Long> courseIds) {

    private static final Pattern EMAIL = Pattern.compile("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$");

    public Professor {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("El nombre del profesor no puede estar vacío");
        }
        name = name.trim();
        if (email == null || !EMAIL.matcher(email.trim()).matches()) {
            throw new IllegalArgumentException("El correo del profesor no es válido");
        }
        email = email.trim().toLowerCase(Locale.ROOT);
        if (courseIds == null || courseIds.isEmpty()) {
            throw new IllegalArgumentException("El profesor debe tener al menos un curso asignado");
        }
        if (courseIds.stream().anyMatch(courseId -> courseId == null || courseId < 1)) {
            throw new IllegalArgumentException("courseIds contiene un identificador inválido");
        }
        courseIds = Set.copyOf(new TreeSet<>(courseIds));
    }
}
