package cr.tec.revisatec.mscore.member.infrastructure.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;

@Entity
@Table(name = "group_members", indexes = {
        @Index(name = "ix_group_members_group_id", columnList = "groupId"),
        @Index(name = "ix_group_members_email", columnList = "email")
})
public class GroupMemberEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long groupId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String email;

    @Column(nullable = true)
    private String githubUser;

    @Column(nullable = false)
    private long createdAt;

    protected GroupMemberEntity() {
    }

    public GroupMemberEntity(Long id, Long groupId, String name, String email, String githubUser, long createdAt) {
        this.id = id;
        this.groupId = groupId;
        this.name = name;
        this.email = email;
        this.githubUser = githubUser;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public Long getGroupId() {
        return groupId;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getGithubUser() {
        return githubUser;
    }

    public long getCreatedAt() {
        return createdAt;
    }
}
