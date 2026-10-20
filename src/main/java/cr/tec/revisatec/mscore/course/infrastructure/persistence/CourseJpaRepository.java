package cr.tec.revisatec.mscore.course.infrastructure.persistence;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseJpaRepository extends JpaRepository<CourseEntity, Long> {

    Page<CourseEntity> findByNameContainingIgnoreCase(String name, Pageable pageable);
}
