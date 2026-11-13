package cr.tec.revisatec.mscore.identity.infrastructure.web;

import cr.tec.revisatec.mscore.identity.application.port.in.IdentityUseCase;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class IdentityController {

    /**
     * Encabezado que APIM coloca después de validar el token de Google (validate-jwt).
     * Cuando está presente tiene prioridad sobre el parámetro email.
     */
    public static final String VERIFIED_EMAIL_HEADER = "X-User-Email";

    private final IdentityUseCase identityUseCase;

    public IdentityController(IdentityUseCase identityUseCase) {
        this.identityUseCase = identityUseCase;
    }

    @GetMapping("/me")
    public IdentityDto me(
            @RequestHeader(name = VERIFIED_EMAIL_HEADER, required = false) String verifiedEmail,
            @RequestParam(name = "email", required = false) String email) {
        String resolvedEmail = verifiedEmail != null && !verifiedEmail.isBlank() ? verifiedEmail : email;
        return IdentityDto.from(identityUseCase.resolve(resolvedEmail));
    }
}
