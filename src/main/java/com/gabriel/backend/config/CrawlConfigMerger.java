package com.gabriel.backend.config;

public class CrawlConfigMerger extends PrecedenceMerger<CrawlConfig> {

    @Override
    public Class<CrawlConfig> getMergeClass() {
        return CrawlConfig.class;
    }

}
