package com.gabriel.backend.crawl.rest.validation;

import java.util.regex.Pattern;

public class AlphanumericValidator implements Validator<String> {

    private static final Pattern ALPHANUMERIC_PATTERN = Pattern.compile("^[A-Za-z0-9]+$");

    private final String fieldName;

    public AlphanumericValidator(String fieldName) {
        this.fieldName = fieldName;
    }

    @Override
    public boolean validate(String value, ValidationResult result) {
        if (value == null) {
            return true;
        }

        if (!ALPHANUMERIC_PATTERN.matcher(value).matches()) {
            result.addError(fieldName + " must contain only letters and numbers");
        }

        return true;
    }

}
