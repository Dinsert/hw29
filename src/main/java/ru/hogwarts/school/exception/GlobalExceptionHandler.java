package ru.hogwarts.school.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<String> handleFacultyNotFound(FacultyNotFoundException e) {
        return createErrorResponse(e.getMessage());
    }

    @ExceptionHandler
    public ResponseEntity<String> handleStudentNotFound(StudentNotFoundException e) {
        return createErrorResponse(e.getMessage());
    }

    private ResponseEntity<String> createErrorResponse(String message) {
        HttpStatus status = HttpStatus.NOT_FOUND;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return ResponseEntity
                .status(status)
                .headers(headers)
                .body(String.format("{\"message\":\"%s\",\"status\":%d}", message, status.value()));
    }
}
