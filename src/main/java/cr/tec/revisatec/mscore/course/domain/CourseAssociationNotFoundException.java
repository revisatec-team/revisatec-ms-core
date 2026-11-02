package cr.tec.revisatec.mscore.course.domain;

public class CourseAssociationNotFoundException extends RuntimeException {

    public CourseAssociationNotFoundException() {
        super("Curso no encontrado");
    }
}
