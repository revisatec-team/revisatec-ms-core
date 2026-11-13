package cr.tec.revisatec.mscore.member.domain;

import java.util.Locale;
import java.util.regex.Pattern;

public record GroupMember(Long id, Long groupId, String name, String email, String githubUser, long createdAt) {

    private static final Pattern EMAIL = Pattern.compile("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$");
    private static final Pattern GITHUB_USER = Pattern.compile("^[A-Za-z0-9][A-Za-z0-9-]{0,38}$");

    public GroupMember {
        if (groupId == null) {
            throw new IllegalArgumentException("groupId es obligatorio");
        }
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("El nombre del integrante no puede estar vacío");
        }
        name = name.trim();
        if (email == null || !EMAIL.matcher(email.trim()).matches()) {
            throw new IllegalArgumentException("El correo del integrante no es válido");
        }
        email = email.trim().toLowerCase(Locale.ROOT);
        if (githubUser != null) {
            githubUser = githubUser.trim();
            if (githubUser.isEmpty()) {
                githubUser = null;
            } else if (!GITHUB_USER.matcher(githubUser).matches()) {
                throw new IllegalArgumentException("El usuario de GitHub no es válido");
            }
        }
    }
}
