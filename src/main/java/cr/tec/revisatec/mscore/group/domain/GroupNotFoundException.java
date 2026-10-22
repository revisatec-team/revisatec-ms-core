package cr.tec.revisatec.mscore.group.domain;

public class GroupNotFoundException extends RuntimeException {

    public GroupNotFoundException(Long id) {
        super("Grupo con id " + id + " no encontrado");
    }
}
