package org.springframework.samples.petclinic.rest.error;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;

import java.util.ArrayList;
import java.util.List;

/**
 * Utility to extract and format validation errors into human-readable messages.
 */
public final class ValidationErrorExtractor {

    private ValidationErrorExtractor() {
        // utility
    }

    public static List<String> extract(ConstraintViolationException ex) {
        List<String> result = new ArrayList<>();
        if (ex == null || ex.getConstraintViolations() == null) {
            return result;
        }
        for (ConstraintViolation<?> v : ex.getConstraintViolations()) {
            String path = v.getPropertyPath() == null ? "" : v.getPropertyPath().toString();
            String message = v.getMessage() == null ? "" : v.getMessage();
            Object invalid = v.getInvalidValue();
            String invalidVal = invalid == null ? "null" : invalid.toString();
            String readablePath = humanizePath(path);
            result.add(readablePath + " " + message + " (value: " + invalidVal + ")");
        }
        return result;
    }

    private static String humanizePath(String path) {
        if (path == null || path.isEmpty()) {
            return "Value";
        }
        // convert camelCase and dot paths to spaced, capitalized words
        String single = path.replace('.', ' ')
            .replaceAll("([a-z])([A-Z])", "$1 $2");
        single = single.trim();
        if (single.isEmpty()) {
            return "Value";
        }
        return Character.toUpperCase(single.charAt(0)) + (single.length() > 1 ? single.substring(1) : "");
    }
}
