package cr.tec.revisatec.mscore.identity.application.port.in;

import cr.tec.revisatec.mscore.identity.domain.Identity;

public interface IdentityUseCase {

    Identity resolve(String email);
}
