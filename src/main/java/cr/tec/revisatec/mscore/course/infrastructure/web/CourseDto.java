package cr.tec.revisatec.mscore.course.infrastructure.web;

import cr.tec.revisatec.mscore.course.domain.Course;
import jakarta.validation.constraints.NotBlank;

public record CourseDto(Long id, @NotBlank(message = "El nombre del curso no puede estar vacío") String name) {

    public static CourseDto from(Course course) {
        return new CourseDto(course.id(), course.name());
    }
}
