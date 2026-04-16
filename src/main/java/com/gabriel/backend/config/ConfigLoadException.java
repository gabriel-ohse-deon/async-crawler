package com.gabriel.backend.config;

public class ConfigLoadException extends Exception {

    public ConfigLoadException() {
        super("Failed to load configuration.");
    }

    public ConfigLoadException(String message) {
        super(message);
    }

    public ConfigLoadException(String message, Throwable cause) {
        super(message, cause);
    }

    public ConfigLoadException(Throwable cause) {
        super("Failed to load configuration.", cause);
    }

}

