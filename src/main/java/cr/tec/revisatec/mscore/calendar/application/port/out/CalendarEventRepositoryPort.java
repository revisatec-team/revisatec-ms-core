package cr.tec.revisatec.mscore.calendar.application.port.out;

import cr.tec.revisatec.mscore.calendar.domain.CalendarEvent;
import cr.tec.revisatec.mscore.course.domain.PageResult;

import java.util.Optional;

public interface CalendarEventRepositoryPort {

    PageResult<CalendarEvent> findAll(int page, int pageSize, String search, Long courseId);

    Optional<CalendarEvent> findById(Long id);

    CalendarEvent save(CalendarEvent event);

    void deleteById(Long id);
}
