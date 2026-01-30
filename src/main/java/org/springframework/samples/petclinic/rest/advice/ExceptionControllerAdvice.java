package org.springframework.samples.petclinic.rest.advice;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.samples.petclinic.rest.controller.BindingErrorsResponse;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.URI;
import java.time.Instant;
import java.util.NoSuchElementException;

/**
 * Global Exception handler for REST controllers.
 * <p>
 * This class handles exceptions thrown by REST controllers and returns appropriate HTTP responses to the client.
 */
@RestControllerAdvice
public class ExceptionControllerAdvice {

    /**
     * Private method for constructing the {@link ProblemDetail} object passing the name and details of the exception
     * class.
     *
     * @param ex     Object referring to the thrown exception.
     * @param status HTTP response status.
     * @param url    URL request.
     */
    private ProblemDetail detailBuild(Exception ex, HttpStatus status, StringBuffer url) {
        ProblemDetail detail = ProblemDetail.forStatus(status);
        detail.setType(URI.create(url.toString()));
        detail.setTitle(ex.getClass().getSimpleName());
        detail.setDetail(ex.getLocalizedMessage());
        detail.setProperty("timestamp", Instant.now());
        return detail;
    }

    /**
     * Handles all general exceptions by returning a 500 Internal Server Error status with error details.
     *
     * @param e       The {@link Exception} to be handled
     * @param request {@link HttpServletRequest} object referring to the current request.
     * @return A {@link ResponseEntity} containing the error information and a 500 Internal Server Error status
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ProblemDetail> handleGeneralException(Exception e, HttpServletRequest request) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        ProblemDetail detail = detailBuild(e, status, request.getRequestURL());
        return ResponseEntity.status(status).body(detail);
    }

    /**
     * Handles {@link DataIntegrityViolationException} which typically indicates database constraint violations. This
     * method returns a 404 Not Found status if an entity does not exist.
     *
     * @param ex      The {@link DataIntegrityViolationException} to be handled
     * @param request {@link HttpServletRequest} object referring to the current request.
     * @return A {@link ResponseEntity} containing the error information and a 404 Not Found status
     */
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ProblemDetail> handleDataIntegrityViolationException(DataIntegrityViolationException ex, HttpServletRequest request) {
        HttpStatus status = HttpStatus.NOT_FOUND;
        ProblemDetail detail = detailBuild(ex, status, request.getRequestURL());
        return ResponseEntity.status(status).body(detail);
    }

    /**
     * Handles exception thrown by Bean Validation on controller methods parameters
     *
     * @param ex      The {@link MethodArgumentNotValidException} to be handled
     * @param request {@link HttpServletRequest} object referring to the current request.
     * @return A {@link ResponseEntity} containing the error information and a 400 Bad Request status.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ProblemDetail> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex, HttpServletRequest request) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        BindingErrorsResponse errors = new BindingErrorsResponse();
        BindingResult bindingResult = ex.getBindingResult();
        if (bindingResult.hasErrors()) {
            errors.addAllErrors(bindingResult);
            ProblemDetail detail = detailBuild(ex, status, request.getRequestURL());
            return ResponseEntity.status(status).body(detail);
        }
        return ResponseEntity.status(status).build();
    }

    // map NoSuchElementException to a 404 Not Found response
    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ProblemDetail> handleNoSuchElementException(NoSuchElementException ex, HttpServletRequest request) {
        HttpStatus status = HttpStatus.NOT_FOUND;
        ProblemDetail detail = detailBuild(ex, status, request.getRequestURL());
        return ResponseEntity.status(status).body(detail);
    }

}
