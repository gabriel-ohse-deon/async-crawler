package com.gabriel.backend.crawl.rest.validation;

import java.util.ArrayList;
import java.util.List;

public class FieldValidator<T> {

    private final String fieldName;
    private final T value;
    private final List<Validator<T>> validators = new ArrayList<>();

    public FieldValidator(String fieldName, T value) {
        this.fieldName = fieldName;
        this.value = value;
    }

    public FieldValidator<T> addValidator(Validator<T> validator) {
        validators.add(validator);
        return this;
    }

    public void validate(ValidationResult result) {
        for (Validator<T> validator : validators) {
            boolean continueChain = validator.validate(value, result);
            if (!continueChain) {
                break;
            }
        }
    }
}

