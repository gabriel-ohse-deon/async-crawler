package com.gabriel.backend.crawl.poller;

import com.gabriel.backend.config.ConfigRegistry;
import com.gabriel.backend.config.PollerConfig;
import com.gabriel.backend.repository.AsyncJsonWriterService;
import com.gabriel.backend.repository.JsonCrawlRepository;

public class CrawlPollerFactory {

    private static final CrawlPollerFactory INSTANCE = new CrawlPollerFactory();

    private CrawlPollerFactory() {
    }

    public static CrawlPollerFactory getInstance() {
        return INSTANCE;
    }

    public CrawlPoller createPooler(String crawlId) {
        AsyncJsonWriterService asyncRepository = new AsyncJsonWriterService(new JsonCrawlRepository());
        PollerConfig config = ConfigRegistry.getInstance().getMergedConfig(PollerConfig.class);
        
        return new CrawlPoller(crawlId, asyncRepository, config.getFlushIntervalMs(), config.getMaxPendingUpdates());
    }

}
