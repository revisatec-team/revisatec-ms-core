package cr.tec.revisatec.mscore.member.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GroupMemberJpaRepository extends JpaRepository<GroupMemberEntity, Long> {

    List<GroupMemberEntity> findByGroupIdOrderByNameAsc(Long groupId);

    List<GroupMemberEntity> findByEmail(String email);

    void deleteByGroupId(Long groupId);
}
