package cr.tec.revisatec.mscore.course.domain;

public record Course(Long id, String name) {

    public Course {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("El nombre del curso no puede estar vacío");
        }
        name = name.trim();
    }
}
