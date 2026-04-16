package com.gabriel.backend.config;

import java.net.URI;
import java.util.Optional;

public class CrawlEnvConfigProvider implements ConfigProvider<CrawlConfig> {

    @Override
    public Optional<CrawlConfig> loadConfig() {
        CrawlConfig cfg = new CrawlConfig();

        String baseUri = System.getenv("BASE_URL");
        String maxDepth = System.getenv("MAX_DEPTH");
        String maxUrls = System.getenv("MAX_URLS");
        String followRelatives = System.getenv("FOLLOW_RELATIVES");
        String port = System.getenv("DEFAULT_PORT");

        if (baseUri != null) {
            cfg.setBaseUri(URI.create(baseUri));
        }
        if (maxDepth != null) {
            try {
                cfg.setMaxDepth(Integer.parseInt(maxDepth));
            } catch (NumberFormatException ignored) {
            }
        }
        if (maxUrls != null) {
            try {
                cfg.setMaxUrls(Integer.parseInt(maxUrls));
            } catch (NumberFormatException ignored) {
            }
        }
        if (followRelatives != null) {
            cfg.setFollowRelativeLinks(Boolean.parseBoolean(followRelatives));
        }
        if(port != null) {
            try {
                cfg.setPort(Integer.parseInt(port));
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
    public Class<CrawlConfig> getConfigClass() {
        return CrawlConfig.class;
    }

}
