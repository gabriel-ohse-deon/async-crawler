package com.gabriel.backend.crawl.rest.validation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ValidationResult {

    private boolean valid = true;
    private final List<String> errors = new ArrayList<>();

    public void addError(String message) {
        valid = false;
        errors.add(message);
    }

    public boolean isValid() {
        return valid;
    }

    public List<String> getErrors() {
        return Collections.unmodifiableList(errors);
    }

    @Override
    public String toString() {
        return valid ? "Validation passed" : String.join(", ", errors);
    }
}
