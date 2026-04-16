package com.gabriel.backend.crawl.rest.validation;

import java.net.MalformedURLException;
import java.net.URL;

public class UrlFormatValidator implements Validator<String> {

    private final String fieldName;

    public UrlFormatValidator(String fieldName) {
        this.fieldName = fieldName;
    }

    @Override
    public boolean validate(String value, ValidationResult result) {
        try {
            if (value != null) {
                new URL(value);
            }
            return true;
        } catch (MalformedURLException e) {
            result.addError(fieldName + " is not a valid URL");
            return false;
        }
    }

}
