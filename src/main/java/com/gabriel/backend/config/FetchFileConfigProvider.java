package com.gabriel.backend.config;

public class FetchFileConfigProvider extends JsonConfigProvider<FetchConfig> {

    public FetchFileConfigProvider() {
        super(FetchConfig.class, "/config/fetch-config.json");
    }

}
