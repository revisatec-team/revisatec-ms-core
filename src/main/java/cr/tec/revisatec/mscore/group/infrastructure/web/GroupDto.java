package cr.tec.revisatec.mscore.group.infrastructure.web;

import cr.tec.revisatec.mscore.group.domain.Group;

public record GroupDto(Long id, String name, String repoUrl, int avance, String estado, Long courseId) {

    public static GroupDto from(Group group) {
        return new GroupDto(
                group.id(), group.name(), group.repoUrl(), group.avance(), group.estado(), group.courseId());
    }
}
