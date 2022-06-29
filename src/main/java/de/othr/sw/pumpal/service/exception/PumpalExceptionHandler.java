package de.othr.sw.pumpal.service.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class PumpalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Object> handleUserNotFound(UserNotFoundException exception, WebRequest request) {
        return new ResponseEntity<Object>(new RestApiError(exception.getMessage(), HttpStatus.NOT_FOUND, LocalDateTime.now()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(WorkoutNotFoundException.class)
    public ResponseEntity<Object> handleWorkoutNotFound(WorkoutNotFoundException exception, WebRequest request) {
        return new ResponseEntity<Object>(new RestApiError(exception.getMessage(), HttpStatus.NOT_FOUND, LocalDateTime.now()), HttpStatus.NOT_FOUND);
    }

    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return new ResponseEntity<Object>(new RestApiError(ex.getMessage(), HttpStatus.NOT_FOUND, LocalDateTime.now()), HttpStatus.NOT_FOUND);
    }
}
