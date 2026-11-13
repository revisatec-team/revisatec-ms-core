package cr.tec.revisatec.mscore.identity.domain;

public class IdentityNotFoundException extends RuntimeException {

    public IdentityNotFoundException(String email) {
        super("El correo " + email + " no está registrado en RevisaTEC");
    }
}
