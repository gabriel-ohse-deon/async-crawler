package com.gabriel.backend.crawl.rest.validation;

import java.util.ArrayList;
import java.util.List;

public class RequestValidator {

    private final List<FieldValidator<?>> fields = new ArrayList<>();

    public <T> RequestValidator field(String name, T value, FieldConfigurator<T> configurator) {
        FieldValidator<T> fv = new FieldValidator<>(name, value);
        configurator.configure(fv);
        fields.add(fv);
        return this;
    }

    public ValidationResult validate() {
        ValidationResult result = new ValidationResult();
        for (FieldValidator<?> field : fields) {
            field.validate(result);
        }
        return result;
    }

    @FunctionalInterface
    public interface FieldConfigurator<T> {
        void configure(FieldValidator<T> field);
    }
}

