package com.gabriel.backend.crawl.rest.validation;

public class NotEmptyValidator implements Validator<String> {

    private final String fieldName;

    public NotEmptyValidator(String fieldName) {
        this.fieldName = fieldName;
    }

    @Override
    public boolean validate(String value, ValidationResult result) {
        if (value == null || value.isBlank()) {
            result.addError(fieldName + " must not be empty");
        }
        return true; // continua
    }
}
