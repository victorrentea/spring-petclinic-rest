package org.springframework.samples.petclinic.rest.error;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.NoSuchElementException;

import static org.springframework.http.HttpStatus.NOT_FOUND;

/**
 * Global Exception handler for REST controllers.
 * <p>
 * This class handles exceptions thrown by REST controllers and returns appropriate HTTP responses to the client.
 */
@Slf4j
@RestControllerAdvice
public class ExceptionControllerAdvice {

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String handleGeneralException(Exception e, HttpServletRequest request) {
        log.error("An unexpected error occurred: {}", e.getMessage(), e);
        return e.getMessage();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<String> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        // return a comma separted list of all validation errors
        BindingResult bindingResult = ex.getBindingResult();
        StringBuilder errors = new StringBuilder("Validation failed: ");
        bindingResult.getFieldErrors().forEach(error ->
            errors.append(String.format("[%s: %s] ", error.getField(), error.getDefaultMessage()))
        );
        log.error("Validation errors: {}", errors.toString());
        return ResponseEntity.badRequest().body(errors.toString());
    }

    // map NoSuchElementException to a 404 Not Found response
    @ExceptionHandler(NoSuchElementException.class)
    @ResponseStatus(NOT_FOUND)
    public String handleNoSuchElementException(NoSuchElementException ex, HttpServletRequest request) {
        log.error("Not found!");
        return "Not found!";
    }

}
