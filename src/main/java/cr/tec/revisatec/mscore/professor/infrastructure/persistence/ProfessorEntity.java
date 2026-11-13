package cr.tec.revisatec.mscore.professor.infrastructure.persistence;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "professors", uniqueConstraints = @UniqueConstraint(name = "uk_professors_email", columnNames = "email"))
public class ProfessorEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String email;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "professor_courses", joinColumns = @JoinColumn(name = "professor_id"))
    @Column(name = "course_id", nullable = false)
    private Set<Long> courseIds = new HashSet<>();

    protected ProfessorEntity() {
    }

    public ProfessorEntity(Long id, String name, String email, Set<Long> courseIds) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.courseIds = new HashSet<>(courseIds);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public Set<Long> getCourseIds() {
        return courseIds;
    }
}
