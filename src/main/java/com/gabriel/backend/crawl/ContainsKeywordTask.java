package com.gabriel.backend.crawl;

import com.gabriel.backend.crawl.action.ContainsKeywordSearcherAction;
import com.gabriel.backend.crawl.event.CrawlListener;
import com.gabriel.backend.crawl.fetch.Fetcher;
import com.gabriel.backend.crawl.fetch.RawLinkExtractorFromRawHTML;
import com.gabriel.backend.crawl.policy.CrawlPolicy;

import java.net.URI;
import java.util.List;
import java.util.Set;

public class ContainsKeywordTask implements Runnable {

    private final LinkItem linkItem;
    private final String keyword;
    private final LinkManager linkManager;
    private final Fetcher fetcher;
    private final CrawlListener crawlListener;
    private final List<CrawlPolicy> policies;
    private final ContainsKeywordSearcherAction action;
    private final RawLinkExtractorFromRawHTML linkExtractor;

    public ContainsKeywordTask(LinkItem linkItem,
                               String keyword,
                               LinkManager linkManager,
                               Fetcher fetcher,
                               CrawlListener crawlListener,
                               List<CrawlPolicy> policies,
                               ContainsKeywordSearcherAction action,
                               RawLinkExtractorFromRawHTML linkExtractor) {
        this.linkItem = linkItem;
        this.keyword = keyword;
        this.linkManager = linkManager;
        this.fetcher = fetcher;
        this.crawlListener = crawlListener;
        this.policies = policies;
        this.action = action;
        this.linkExtractor = linkExtractor;
    }

    @Override
    public void run() {
        URI uri = linkItem.getAbsoluteNormalizedUri();
        try {
            fetcher.fetch(uri).ifPresentOrElse(html -> {
                Set<String> rawLinks = (Set<String>) linkExtractor.extractLinks(html);
                linkManager.addLinks(linkItem, rawLinks, policies);
                action.search(html, keyword.trim()).ifPresent(found -> {
                    if (found) {
                        crawlListener.onFound(uri);
                    }
                });
            }, () -> crawlListener.onFailed(uri));
        } catch (Exception e) {
            crawlListener.onError(uri, e);
        }
    }
}


