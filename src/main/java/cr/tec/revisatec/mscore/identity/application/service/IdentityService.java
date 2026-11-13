package cr.tec.revisatec.mscore.identity.application.service;

import cr.tec.revisatec.mscore.course.application.port.out.CourseRepositoryPort;
import cr.tec.revisatec.mscore.course.domain.Course;
import cr.tec.revisatec.mscore.group.application.port.out.GroupRepositoryPort;
import cr.tec.revisatec.mscore.identity.application.port.in.IdentityUseCase;
import cr.tec.revisatec.mscore.identity.domain.Identity;
import cr.tec.revisatec.mscore.identity.domain.IdentityNotFoundException;
import cr.tec.revisatec.mscore.member.application.port.out.GroupMemberRepositoryPort;
import cr.tec.revisatec.mscore.member.domain.GroupMember;
import cr.tec.revisatec.mscore.professor.application.port.out.ProfessorRepositoryPort;
import cr.tec.revisatec.mscore.professor.domain.Professor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class IdentityService implements IdentityUseCase {

    private final ProfessorRepositoryPort professorRepository;
    private final GroupMemberRepositoryPort memberRepository;
    private final GroupRepositoryPort groupRepository;
    private final CourseRepositoryPort courseRepository;

    public IdentityService(
            ProfessorRepositoryPort professorRepository,
            GroupMemberRepositoryPort memberRepository,
            GroupRepositoryPort groupRepository,
            CourseRepositoryPort courseRepository) {
        this.professorRepository = professorRepository;
        this.memberRepository = memberRepository;
        this.groupRepository = groupRepository;
        this.courseRepository = courseRepository;
    }

    @Override
    public Identity resolve(String email) {
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("email es obligatorio");
        }
        String normalizedEmail = email.trim().toLowerCase(Locale.ROOT);

        Optional<Professor> professor = professorRepository.findByEmail(normalizedEmail);
        if (professor.isPresent()) {
            List<Course> courses = professor.get().courseIds().stream()
                    .map(courseRepository::findById)
                    .flatMap(Optional::stream)
                    .sorted(Comparator.comparing(Course::id))
                    .toList();
            return new Identity(Identity.Role.PROFESSOR, professor.get().name(), normalizedEmail, courses, List.of());
        }

        List<GroupMember> members = memberRepository.findByEmail(normalizedEmail);
        List<Identity.Membership> memberships = members.stream()
                .flatMap(member -> groupRepository.findById(member.groupId()).stream()
                        .flatMap(group -> courseRepository.findById(group.courseId()).stream()
                                .map(course -> new Identity.Membership(course, group, member))))
                .sorted(Comparator.comparing(membership -> membership.course().id()))
                .toList();
        if (memberships.isEmpty()) {
            throw new IdentityNotFoundException(normalizedEmail);
        }
        List<Course> courses = memberships.stream().map(Identity.Membership::course).distinct().toList();
        return new Identity(
                Identity.Role.STUDENT, memberships.getFirst().member().name(), normalizedEmail, courses, memberships);
    }
}
