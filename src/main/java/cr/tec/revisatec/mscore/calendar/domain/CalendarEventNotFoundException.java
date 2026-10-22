package cr.tec.revisatec.mscore.calendar.domain;

public class CalendarEventNotFoundException extends RuntimeException {

    public CalendarEventNotFoundException(Long id) {
        super("Recurso no encontrado");
    }
}
