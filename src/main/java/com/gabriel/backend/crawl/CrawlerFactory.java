package com.gabriel.backend.crawl;

import com.gabriel.backend.config.ConfigRegistry;
import com.gabriel.backend.config.CrawlConfig;
import com.gabriel.backend.crawl.action.SearcherActionType;
import com.gabriel.backend.crawl.event.CrawlListener;
import com.gabriel.backend.crawl.fetch.Fetcher;
import com.gabriel.backend.crawl.fetch.FetcherFactory;
import com.gabriel.backend.crawl.fetch.FetcherType;
import com.gabriel.backend.crawl.policy.CrawlPolicy;

import java.util.List;
import java.util.ServiceLoader;

public class CrawlerFactory {

    private static final CrawlerFactory INSTANCE = new CrawlerFactory();

    private CrawlerFactory() {}

    public static CrawlerFactory getInstance() {
        return INSTANCE;
    }

    public Crawler createCrawler(String crawlId,

                                 FetcherType fetcherType,
                                 CrawlListener eventManager,
                                 SearcherActionType actionType) {

        CrawlConfig crawlConfig = ConfigRegistry.getInstance().getMergedConfig(CrawlConfig.class);
        Fetcher fetcher = FetcherFactory.getInstance().createFetcher(fetcherType);

        List<CrawlPolicy> policies = ServiceLoader.load(CrawlPolicy.class)
                .stream()
                .map(ServiceLoader.Provider::get)
                .toList();

        return new Crawler(
                crawlId,
                new LinkManager(),
                fetcher,
                eventManager,
                policies,
                actionType,
                crawlConfig.getMaxThreads()
        );
    }
}





