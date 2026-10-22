package cr.tec.revisatec.mscore.group.infrastructure.persistence;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GroupJpaRepository extends JpaRepository<GroupEntity, Long> {

    Page<GroupEntity> findByNameContainingIgnoreCase(String name, Pageable pageable);
}
