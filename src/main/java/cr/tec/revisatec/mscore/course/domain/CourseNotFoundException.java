package cr.tec.revisatec.mscore.course.domain;

public class CourseNotFoundException extends RuntimeException {

    public CourseNotFoundException(Long id) {
        super("No se encontró el curso con id " + id);
    }
}
