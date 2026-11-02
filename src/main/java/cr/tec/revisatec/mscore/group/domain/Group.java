package cr.tec.revisatec.mscore.group.domain;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Set;

public record Group(Long id, String name, String repoUrl, int avance, String estado, Long courseId) {

    private static final Set<String> VALID_STATES = Set.of("Pendiente", "En progreso", "Completado");

    public Group {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("El nombre del grupo no puede estar vacío");
        }
        name = name.trim();
        if (repoUrl != null) {
            repoUrl = repoUrl.trim();
        }
        if (!isValidRepoUrl(repoUrl)) {
            throw new IllegalArgumentException("La URL del repositorio debe ser una URL http o https válida");
        }
        if (avance < 0 || avance > 100) {
            throw new IllegalArgumentException("avance debe estar entre 0 y 100");
        }
        if (estado == null || !VALID_STATES.contains(estado)) {
            throw new IllegalArgumentException("estado debe ser Pendiente, En progreso o Completado");
        }
    }

    private static boolean isValidRepoUrl(String value) {
        if (value == null || value.isBlank()) {
            return false;
        }
        try {
            URI uri = new URI(value.trim());
            String scheme = uri.getScheme();
            return uri.isAbsolute()
                    && uri.getHost() != null
                    && ("http".equalsIgnoreCase(scheme) || "https".equalsIgnoreCase(scheme));
        } catch (URISyntaxException exception) {
            return false;
        }
    }
}
