package cr.tec.revisatec.mscore.calendar.infrastructure.web;

import cr.tec.revisatec.mscore.calendar.domain.CalendarEventNotFoundException;
import cr.tec.revisatec.mscore.course.domain.CourseAssociationNotFoundException;
import cr.tec.revisatec.mscore.course.infrastructure.web.GlobalExceptionHandler.ErrorResponse;
import jakarta.validation.ConstraintViolationException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestControllerAdvice(assignableTypes = CalendarEventController.class)
@Order(Ordered.HIGHEST_PRECEDENCE)
public class CalendarEventExceptionHandler {

    @ExceptionHandler(CalendarEventNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(CalendarEventNotFoundException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse("Recurso no encontrado"));
    }

    @ExceptionHandler(CourseAssociationNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleAssociatedCourseNotFound(
            CourseAssociationNotFoundException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse(exception.getMessage()));
    }

    @ExceptionHandler({
            IllegalArgumentException.class,
            ConstraintViolationException.class,
            MethodArgumentTypeMismatchException.class
    })
    public ResponseEntity<ErrorResponse> handleBadRequest(Exception exception) {
        return ResponseEntity.badRequest().body(new ErrorResponse(exception.getMessage()));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleUnreadableBody(HttpMessageNotReadableException exception) {
        return ResponseEntity.badRequest().body(new ErrorResponse("Solicitud inválida"));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException exception) {
        FieldError fieldError = exception.getBindingResult().getFieldErrors().stream()
                .findFirst()
                .orElse(null);
        String message = fieldError == null ? "Solicitud inválida" : fieldError.getDefaultMessage();
        return ResponseEntity.badRequest().body(new ErrorResponse(message));
    }
}
