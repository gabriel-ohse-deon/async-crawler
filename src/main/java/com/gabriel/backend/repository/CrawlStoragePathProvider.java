package com.gabriel.backend.repository;

public class CrawlStoragePathProvider {

    private static final String ENV_VAR = "CRAWL_DATA_DIR";
    private static final String DEFAULT_PATH = "data/crawls";

    public String getBasePath() {
        return System.getenv().getOrDefault(ENV_VAR, DEFAULT_PATH);
    }

}
