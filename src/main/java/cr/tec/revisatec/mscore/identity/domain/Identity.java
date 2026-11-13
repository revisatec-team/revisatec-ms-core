package cr.tec.revisatec.mscore.identity.domain;

import cr.tec.revisatec.mscore.course.domain.Course;
import cr.tec.revisatec.mscore.group.domain.Group;
import cr.tec.revisatec.mscore.member.domain.GroupMember;

import java.util.List;

/**
 * Quién es la persona con sesión iniciada y a qué tiene acceso.
 * Un profesor ve sus cursos; un estudiante ve los cursos donde pertenece a un grupo.
 */
public record Identity(Role role, String name, String email, List<Course> courses, List<Membership> memberships) {

    public enum Role { PROFESSOR, STUDENT }

    public record Membership(Course course, Group group, GroupMember member) {
    }
}
