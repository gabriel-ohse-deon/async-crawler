package com.gabriel.backend.config;

import java.util.Optional;

public class PollerEnvConfigProvider implements ConfigProvider<PollerConfig> {

    @Override
    public Optional<PollerConfig> loadConfig() {
        PollerConfig cfg = new PollerConfig();

        String poolFlushInterval = System.getenv("POOL_FLUSH_INTERVAL");
        String maxPendingUpdates = System.getenv("POOL_MAX_PENDING_UPDATES");

        if (poolFlushInterval != null) {
            try {
                cfg.setFlushIntervalMs(Integer.parseInt(poolFlushInterval));
            } catch (NumberFormatException ignored) {
            }
        }
        if (maxPendingUpdates != null) {
            try {
                cfg.setMaxPendingUpdates(Integer.parseInt(maxPendingUpdates));
            } catch (NumberFormatException ignored) {
            }
        }

        return Optional.of(cfg);
    }

    @Override
    public ConfigSourceType getSourceType() {
        return ConfigSourceType.ENV;
    }

    @Override
    public Class<PollerConfig> getConfigClass() {
        return PollerConfig.class;
    }

}
