package cr.tec.revisatec.mscore.calendar.infrastructure.web;

import cr.tec.revisatec.mscore.calendar.application.port.in.CalendarEventUseCase;
import cr.tec.revisatec.mscore.calendar.domain.CalendarEvent;
import cr.tec.revisatec.mscore.course.domain.PageResult;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/calendar/events")
public class CalendarEventController {

    private final CalendarEventUseCase eventUseCase;

    public CalendarEventController(CalendarEventUseCase eventUseCase) {
        this.eventUseCase = eventUseCase;
    }

    @GetMapping
    public PageResult<CalendarEventDto> listEvents(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(defaultValue = "") String search,
            @RequestParam(required = false) Long courseId) {
        PageResult<CalendarEvent> result = eventUseCase.listEvents(page, pageSize, search, courseId);
        List<CalendarEventDto> items = result.items().stream().map(CalendarEventDto::from).toList();
        return new PageResult<>(result.totalRecords(), result.page(), result.pageSize(), items);
    }

    @PostMapping
    public ResponseEntity<CalendarEventDto> createEvent(@Valid @RequestBody CalendarEventRequestDto request) {
        CalendarEvent event = eventUseCase.createEvent(
                request.title(), request.date(), request.audience(), request.type(), request.courseId());
        return ResponseEntity.status(HttpStatus.CREATED).body(CalendarEventDto.from(event));
    }

    @PutMapping("/{id}")
    public CalendarEventDto updateEvent(
            @PathVariable Long id,
            @Valid @RequestBody CalendarEventRequestDto request) {
        return CalendarEventDto.from(eventUseCase.updateEvent(
                id, request.title(), request.date(), request.audience(), request.type(), request.courseId()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<DeleteResponse> deleteEvent(@PathVariable Long id) {
        eventUseCase.deleteEvent(id);
        return ResponseEntity.ok(new DeleteResponse("Evento eliminado"));
    }

    public record DeleteResponse(String message) {
    }
}
