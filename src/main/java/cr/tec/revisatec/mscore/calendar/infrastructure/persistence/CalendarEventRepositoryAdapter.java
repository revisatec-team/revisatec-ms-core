package cr.tec.revisatec.mscore.calendar.infrastructure.persistence;

import cr.tec.revisatec.mscore.calendar.application.port.out.CalendarEventRepositoryPort;
import cr.tec.revisatec.mscore.calendar.domain.CalendarEvent;
import cr.tec.revisatec.mscore.course.domain.PageResult;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class CalendarEventRepositoryAdapter implements CalendarEventRepositoryPort {

    private final CalendarEventJpaRepository repository;

    public CalendarEventRepositoryAdapter(CalendarEventJpaRepository repository) {
        this.repository = repository;
    }

    @Override
    public PageResult<CalendarEvent> findAll(int page, int pageSize, String search) {
        PageRequest pageable = PageRequest.of(page - 1, pageSize);
        Page<CalendarEventEntity> result = search.isBlank()
                ? repository.findAllByOrderByDateAsc(pageable)
                : repository.findByTitleContainingIgnoreCaseOrderByDateAsc(search, pageable);
        return new PageResult<>(
                result.getTotalElements(),
                page,
                pageSize,
                result.getContent().stream().map(CalendarEventRepositoryAdapter::toDomain).toList());
    }

    @Override
    public Optional<CalendarEvent> findById(Long id) {
        return repository.findById(id).map(CalendarEventRepositoryAdapter::toDomain);
    }

    @Override
    public CalendarEvent save(CalendarEvent event) {
        CalendarEventEntity entity = repository.save(new CalendarEventEntity(
                event.id(), event.title(), event.date(), event.audience(), event.type()));
        return toDomain(entity);
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    private static CalendarEvent toDomain(CalendarEventEntity entity) {
        return new CalendarEvent(
                entity.getId(), entity.getTitle(), entity.getDate(), entity.getAudience(), entity.getType());
    }
}
