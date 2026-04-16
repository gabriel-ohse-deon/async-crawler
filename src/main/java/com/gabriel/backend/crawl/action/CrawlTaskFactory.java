package com.gabriel.backend.crawl.action;

import com.gabriel.backend.crawl.ContainsKeywordTask;
import com.gabriel.backend.crawl.LinkItem;
import com.gabriel.backend.crawl.LinkManager;
import com.gabriel.backend.crawl.event.CrawlListener;
import com.gabriel.backend.crawl.fetch.Fetcher;
import com.gabriel.backend.crawl.fetch.RawLinkExtractorFromRawHTML;
import com.gabriel.backend.crawl.policy.CrawlPolicy;

import java.util.List;
import java.util.ServiceLoader;

public class CrawlTaskFactory {

    private static final CrawlTaskFactory INSTANCE = new CrawlTaskFactory();

    private CrawlTaskFactory() {}

    public static CrawlTaskFactory getInstance() {
        return INSTANCE;
    }

    public Runnable createTask(LinkItem linkItem,
                               String keyword,
                               Fetcher fetcher,
                               CrawlListener crawlListener,
                               LinkManager linkManager,
                               List<CrawlPolicy> policies,
                               SearcherActionType searcherType) {

        CrawlSearchAction<?> action = ServiceLoader.load(CrawlSearchAction.class)
                .stream()
                .map(ServiceLoader.Provider::get)
                .filter(a -> matchesType(a, searcherType))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                        "No search action found for: " + searcherType));

        return switch (searcherType) {
            case CONTAINS_KEYWORD -> new ContainsKeywordTask(
                    linkItem,
                    keyword,
                    linkManager,
                    fetcher,
                    crawlListener,
                    policies,
                    (ContainsKeywordSearcherAction) action,
                    new RawLinkExtractorFromRawHTML()
            );
        };
    }

    private static boolean matchesType(CrawlSearchAction<?> searcher, SearcherActionType type) {
        return switch (type) {
            case CONTAINS_KEYWORD -> searcher instanceof ContainsKeywordSearcherAction;
        };
    }
}

