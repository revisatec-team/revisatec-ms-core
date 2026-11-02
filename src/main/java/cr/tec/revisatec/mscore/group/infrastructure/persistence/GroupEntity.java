package cr.tec.revisatec.mscore.group.infrastructure.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "course_groups")
public class GroupEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String repoUrl;

    @Column(nullable = false)
    private int avance;

    @Column(nullable = false)
    private String estado;

    @Column(nullable = true)
    private Long courseId;

    protected GroupEntity() {
    }

    public GroupEntity(Long id, String name, String repoUrl, int avance, String estado, Long courseId) {
        this.id = id;
        this.name = name;
        this.repoUrl = repoUrl;
        this.avance = avance;
        this.estado = estado;
        this.courseId = courseId;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getRepoUrl() {
        return repoUrl;
    }

    public int getAvance() {
        return avance;
    }

    public String getEstado() {
        return estado;
    }

    public Long getCourseId() {
        return courseId;
    }
}
