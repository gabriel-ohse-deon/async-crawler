package com.gabriel.backend.crawl.policy;

import com.gabriel.backend.crawl.LinkItem;

public interface CrawlPolicy {
    boolean shouldVisit(LinkItem item);
}
