package cr.tec.revisatec.mscore.calendar.infrastructure.web;

import cr.tec.revisatec.mscore.calendar.domain.CalendarEvent;

public record CalendarEventDto(Long id, String title, Long date, String audience, String type, Long courseId) {

    public static CalendarEventDto from(CalendarEvent event) {
        return new CalendarEventDto(
                event.id(), event.title(), event.date(), event.audience(), event.type(), event.courseId());
    }
}
