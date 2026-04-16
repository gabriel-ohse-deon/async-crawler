package com.gabriel.backend.config;

import com.google.gson.Gson;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

public abstract class JsonConfigProvider<T> implements ConfigProvider<T> {

    private final Class<T> type;
    private final String resourcePath;
    private final Gson gson = new Gson();

    public JsonConfigProvider(Class<T> type, String resourcePath) {
        this.type = type;
        this.resourcePath = resourcePath;
    }

    @Override
    public Optional<T> loadConfig() throws ConfigLoadException {
        String path = resourcePath.startsWith("/") ? resourcePath : "/" + resourcePath;

        try (InputStream in = getClass().getResourceAsStream(path)) {
            if (in == null) {
                return Optional.empty();
            }

            try (Reader reader = new InputStreamReader(in, StandardCharsets.UTF_8)) {
                T config = gson.fromJson(reader, type);
                return Optional.ofNullable(config);
            }

        } catch (Exception e) {
            throw new ConfigLoadException("Error loading configuration from resource: " + path, e);
        }
    }

    @Override
    public ConfigSourceType getSourceType() {
        return ConfigSourceType.JSON_FILE;
    }

    @Override
    public Class<T> getConfigClass() {
        return type;
    }
}
