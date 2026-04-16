package com.gabriel.backend.config;

public class FetchConfigMerger extends PrecedenceMerger<FetchConfig> {

    @Override
    public Class<FetchConfig> getMergeClass() {
        return FetchConfig.class;
    }

}
