package antonioschettini.u5_w3_d5.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {
    // 400
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorsPayload handleValidation(MethodArgumentNotValidException ex) {
        List<String> errori = ex.getBindingResult().getFieldErrors().stream()
                .map(FieldError::getDefaultMessage).toList();
        return new ErrorsPayload("Dati inseriti non validi!", LocalDateTime.now(), errori);
    }

    // 400
    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorsPayload handleBadRequest(BadRequestException ex) {
        return new ErrorsPayload("Operazione non valida", LocalDateTime.now(), List.of(ex.getMessage()));
    }

    // 401
    @ExceptionHandler(UnauthorizedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorsPayload handleUnauthorized(UnauthorizedException ex) {
        return new ErrorsPayload("Non sei autorizzato", LocalDateTime.now(), List.of(ex.getMessage()));
    }

    // 403
    @ExceptionHandler(ForbiddenException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorsPayload handleForbidden(ForbiddenException ex) {
        return new ErrorsPayload("Accesso negato", LocalDateTime.now(), List.of(ex.getMessage()));
    }

    // 403
    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorsPayload handleSpringSecurityForbidden(AccessDeniedException ex) {
        return new ErrorsPayload("Accesso negato", LocalDateTime.now(), List.of("Non hai i permessi necessari per questa azione"));
    }

    // 404
    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorsPayload handleNotFound(NotFoundException ex) {
        return new ErrorsPayload("Risorsa non trovata a db", LocalDateTime.now(), List.of(ex.getMessage()));
    }

    // 500
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorsPayload handleGeneric(Exception ex) {
        ex.printStackTrace(); // tengo traccia degli errori
        return new ErrorsPayload("Errore interno del server", LocalDateTime.now(), List.of("Si è verificato un problema tecnico temporaneo"));
    }
}

