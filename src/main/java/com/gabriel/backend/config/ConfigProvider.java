package com.gabriel.backend.config;

import java.util.Optional;

public interface ConfigProvider<T> {
    Optional<T> loadConfig() throws ConfigLoadException;
    ConfigSourceType getSourceType();
    Class<T> getConfigClass();
}
