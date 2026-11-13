package cr.tec.revisatec.mscore.member.domain;

public class DuplicateMemberException extends RuntimeException {

    public DuplicateMemberException(String email) {
        super("El correo " + email + " ya pertenece a un grupo de este curso");
    }
}
