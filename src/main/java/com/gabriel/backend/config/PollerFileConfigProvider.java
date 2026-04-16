package com.gabriel.backend.config;

public class PollerFileConfigProvider extends JsonConfigProvider<PollerConfig> {

    public PollerFileConfigProvider() {
        super(PollerConfig.class, "/config/poller-config.json");
    }

}
