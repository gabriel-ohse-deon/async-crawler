package com.gabriel.backend.crawl.rest.validation;

public class NullValidator<T> implements Validator<T> {

    private final String fieldName;

    public NullValidator(String fieldName) {
        this.fieldName = fieldName;
    }

    @Override
    public boolean validate(T value, ValidationResult result) {
        if (value == null) {
            result.addError(fieldName + " must not be null");
            return false; // interrompe a cadeia
        }
        return true; // continua
    }
}
