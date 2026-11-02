package cr.tec.revisatec.mscore.calendar.application.service;

import cr.tec.revisatec.mscore.calendar.application.port.in.CalendarEventUseCase;
import cr.tec.revisatec.mscore.calendar.application.port.out.CalendarEventRepositoryPort;
import cr.tec.revisatec.mscore.calendar.domain.CalendarEvent;
import cr.tec.revisatec.mscore.calendar.domain.CalendarEventNotFoundException;
import cr.tec.revisatec.mscore.course.application.port.out.CourseRepositoryPort;
import cr.tec.revisatec.mscore.course.domain.CourseAssociationNotFoundException;
import cr.tec.revisatec.mscore.course.domain.PageResult;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CalendarEventService implements CalendarEventUseCase {

    private static final int MAX_PAGE_SIZE = 100;

    private final CalendarEventRepositoryPort eventRepository;
    private final CourseRepositoryPort courseRepository;

    public CalendarEventService(
            CalendarEventRepositoryPort eventRepository,
            CourseRepositoryPort courseRepository) {
        this.eventRepository = eventRepository;
        this.courseRepository = courseRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public PageResult<CalendarEvent> listEvents(int page, int pageSize, String search, Long courseId) {
        if (page < 1) {
            throw new IllegalArgumentException("page debe ser mayor o igual a 1");
        }
        if (pageSize < 1 || pageSize > MAX_PAGE_SIZE) {
            throw new IllegalArgumentException("pageSize debe estar entre 1 y 100");
        }

        PageResult<CalendarEvent> result = eventRepository.findAll(
                page, pageSize, search == null ? "" : search.trim(), courseId);
        long lastPage = Math.max(1, (result.totalRecords() + pageSize - 1) / pageSize);
        if (page > lastPage) {
            throw new IllegalArgumentException("page está fuera del rango disponible");
        }
        return result;
    }

    @Override
    public CalendarEvent createEvent(String title, Long date, String audience, String type, Long courseId) {
        if (courseId == null) {
            throw new IllegalArgumentException("courseId es obligatorio");
        }
        validateCourse(courseId);
        return eventRepository.save(new CalendarEvent(
                null, title, date, audience, type == null ? "blue" : type, courseId));
    }

    @Override
    public CalendarEvent updateEvent(
            Long id, String title, Long date, String audience, String type, Long courseId) {
        if (type == null) {
            throw new IllegalArgumentException("type es obligatorio");
        }
        CalendarEvent existingEvent = findEvent(id);
        Long updatedCourseId = courseId == null ? existingEvent.courseId() : courseId;
        validateCourse(updatedCourseId);
        return eventRepository.save(
                new CalendarEvent(existingEvent.id(), title, date, audience, type, updatedCourseId));
    }

    @Override
    public void deleteEvent(Long id) {
        findEvent(id);
        eventRepository.deleteById(id);
    }

    private CalendarEvent findEvent(Long id) {
        return eventRepository.findById(id).orElseThrow(() -> new CalendarEventNotFoundException(id));
    }

    private void validateCourse(Long courseId) {
        if (courseId == null || courseRepository.findById(courseId).isEmpty()) {
            throw new CourseAssociationNotFoundException();
        }
    }
}
