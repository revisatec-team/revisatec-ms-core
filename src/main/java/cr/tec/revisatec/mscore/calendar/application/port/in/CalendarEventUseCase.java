package cr.tec.revisatec.mscore.calendar.application.port.in;

import cr.tec.revisatec.mscore.calendar.domain.CalendarEvent;
import cr.tec.revisatec.mscore.course.domain.PageResult;

public interface CalendarEventUseCase {

    PageResult<CalendarEvent> listEvents(int page, int pageSize, String search);

    CalendarEvent createEvent(String title, Long date, String audience, String type);

    CalendarEvent updateEvent(Long id, String title, Long date, String audience, String type);

    void deleteEvent(Long id);
}
