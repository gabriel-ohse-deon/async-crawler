package com.gabriel.backend.crawl.rest.validation;

public class LengthBetweenValidator implements Validator<String> {

    private final String fieldName;
    private final int min;
    private final int max;

    public LengthBetweenValidator(String fieldName, int min, int max) {
        this.fieldName = fieldName;
        this.min = min;
        this.max = max;
    }

    @Override
    public boolean validate(String value, ValidationResult result) {
        if (value.length() < min || value.length() > max) {
            result.addError(fieldName + " length must be between " + min + " and " + max);
        }
        return true;
    }
}

