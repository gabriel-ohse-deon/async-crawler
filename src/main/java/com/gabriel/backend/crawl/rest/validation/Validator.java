package com.gabriel.backend.crawl.rest.validation;

@FunctionalInterface
public interface Validator<T> {

    boolean validate(T value, ValidationResult result);
}
