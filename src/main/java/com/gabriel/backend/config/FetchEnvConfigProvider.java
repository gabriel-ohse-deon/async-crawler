package com.gabriel.backend.config;

import java.util.Optional;

public class FetchEnvConfigProvider implements ConfigProvider<FetchConfig> {

    @Override
    public Optional<FetchConfig> loadConfig() {
        FetchConfig cfg = new FetchConfig();

        String timeout = System.getenv("FETCH_TIMEOUT");
        String connectionTimeout = System.getenv("FETCH_CONN_TIMEOUT");
        String userAgent = System.getenv("FETCH_USER_AGENT");

        if (timeout != null) {
            try {
                cfg.setFetchTimeoutMs(Integer.parseInt(timeout));
            } catch (NumberFormatException ignored) {
            }
        }
        if (connectionTimeout != null) {
            try {
                cfg.setConnectionTimeoutMs(Integer.parseInt(connectionTimeout));
            } catch (NumberFormatException ignored) {
            }
        }
        if (userAgent != null) cfg.setUserAgent(userAgent);


        return Optional.of(cfg);
    }

    @Override
    public ConfigSourceType getSourceType() {
        return ConfigSourceType.ENV;
    }

    @Override
    public Class<FetchConfig> getConfigClass() {
        return FetchConfig.class;
    }
}

