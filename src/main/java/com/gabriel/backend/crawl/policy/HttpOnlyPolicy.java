package com.gabriel.backend.crawl.policy;

import com.gabriel.backend.crawl.LinkItem;

import java.net.URI;

public class HttpOnlyPolicy implements CrawlPolicy {

    @Override
    public boolean shouldVisit(LinkItem item) {
        URI uri = item.getAbsoluteNormalizedUri();
        String scheme = uri.getScheme();
        return scheme != null
                && (scheme.equalsIgnoreCase("http") || scheme.equalsIgnoreCase("https"));
    }

}

