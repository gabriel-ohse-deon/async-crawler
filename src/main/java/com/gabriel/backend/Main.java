package com.gabriel.backend;

import com.gabriel.backend.config.ConfigRegistry;
import com.gabriel.backend.config.ConfigSourceOrder;
import com.gabriel.backend.config.CrawlConfig;
import com.gabriel.backend.crawl.IdGenerator;
import com.gabriel.backend.crawl.poller.PollerManager;
import com.gabriel.backend.crawl.rest.CrawlRestService;
import com.gabriel.backend.repository.JsonCrawlRepository;

public class Main {
    public static void main(String[] args) {
        ConfigRegistry.getInstance().initializeOrExit(ConfigSourceOrder.ENV_THEN_ARQ);
        Integer defaultPort = ConfigRegistry.getInstance().getMergedConfig(CrawlConfig.class).getPort();

        JsonCrawlRepository jsonCrawlRepository = new JsonCrawlRepository();
        PollerManager pollerManager = new PollerManager();
        IdGenerator idGenerator = new IdGenerator();

        CrawlRestService restService = new CrawlRestService(jsonCrawlRepository, pollerManager, idGenerator, defaultPort);
        restService.initRoutes();
    }
}
