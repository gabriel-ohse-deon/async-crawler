package com.gabriel.backend.config;

public class CrawlFileConfigProvider extends JsonConfigProvider<CrawlConfig> {

    public CrawlFileConfigProvider() {
        super(CrawlConfig.class, "/config/crawl-config.json");
    }

}
