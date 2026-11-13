package cr.tec.revisatec.mscore.professor.domain;

public class ProfessorNotFoundException extends RuntimeException {

    public ProfessorNotFoundException(Long id) {
        super("Profesor con id " + id + " no encontrado");
    }
}
