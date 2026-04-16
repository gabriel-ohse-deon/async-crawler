package com.gabriel.backend.config;

public class CrawlPoolerConfigMerger extends PrecedenceMerger<PollerConfig> {

    @Override
    public Class<PollerConfig> getMergeClass() {
        return PollerConfig.class;
    }

}
