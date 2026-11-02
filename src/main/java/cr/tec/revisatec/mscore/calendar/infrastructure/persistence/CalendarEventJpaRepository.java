package cr.tec.revisatec.mscore.calendar.infrastructure.persistence;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CalendarEventJpaRepository extends JpaRepository<CalendarEventEntity, Long> {

    Page<CalendarEventEntity> findAllByOrderByDateAsc(Pageable pageable);

    Page<CalendarEventEntity> findByTitleContainingIgnoreCaseOrderByDateAsc(String title, Pageable pageable);

    Page<CalendarEventEntity> findByCourseIdOrderByDateAsc(Long courseId, Pageable pageable);

    Page<CalendarEventEntity> findByTitleContainingIgnoreCaseAndCourseIdOrderByDateAsc(
            String title, Long courseId, Pageable pageable);
}
