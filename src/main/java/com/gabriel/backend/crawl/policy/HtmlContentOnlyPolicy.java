package com.gabriel.backend.crawl.policy;

import com.gabriel.backend.crawl.LinkItem;

public class HtmlContentOnlyPolicy implements CrawlPolicy {

    @Override
    public boolean shouldVisit(LinkItem item) {
        String url = item.getAbsoluteNormalizedUri().toString().toLowerCase();
        return !(url.endsWith(".css") || url.endsWith(".js") || url.endsWith(".png")
                || url.endsWith(".jpg") || url.endsWith(".jpeg") || url.endsWith(".gif")
                || url.endsWith(".ico") || url.endsWith(".svg"));
    }

}
