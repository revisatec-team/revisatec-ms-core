package cr.tec.revisatec.mscore.professor.domain;

public class DuplicateProfessorException extends RuntimeException {

    public DuplicateProfessorException(String email) {
        super("Ya existe un profesor con el correo " + email);
    }
}
