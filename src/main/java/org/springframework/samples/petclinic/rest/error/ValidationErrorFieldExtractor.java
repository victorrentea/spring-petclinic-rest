package org.springframework.samples.petclinic.rest.error;

import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.ArrayList;
import java.util.List;

public final class ValidationErrorFieldExtractor {

    private ValidationErrorFieldExtractor() {}

    public static List<String> extract(BindingResult bindingResult) {
        List<String> result = new ArrayList<>();
        if (bindingResult == null) return result;
        for (FieldError fe : bindingResult.getFieldErrors()) {
            String field = humanizePath(fe.getField());
            String message = fe.getDefaultMessage() == null ? "" : fe.getDefaultMessage();
            Object rejected = fe.getRejectedValue();
            String val = rejected == null ? "null" : rejected.toString();
            result.add(field + " " + message + " (value: " + val + ")");
        }
        return result;
    }

    private static String humanizePath(String path) {
        if (path == null || path.isEmpty()) return "Value";
        String single = path.replaceAll("([a-z])([A-Z])", "$1 $2");
        single = single.replace('.', ' ').trim();
        return Character.toUpperCase(single.charAt(0)) + (single.length() > 1 ? single.substring(1) : "");
    }
}
