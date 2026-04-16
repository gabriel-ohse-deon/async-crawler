package com.gabriel.backend.crawl.policy;

import com.gabriel.backend.config.ConfigRegistry;
import com.gabriel.backend.config.CrawlConfig;
import com.gabriel.backend.crawl.LinkItem;

public class RelativeLinksPolicy implements CrawlPolicy {

    private final boolean isFollowRelativeLinks;

    public RelativeLinksPolicy() {
        CrawlConfig crawlConfig = ConfigRegistry.getInstance().getMergedConfig(CrawlConfig.class);
        isFollowRelativeLinks = crawlConfig.isFollowRelativeLinks();
    }

    @Override
    public boolean shouldVisit(LinkItem linkItem) {
        return !linkItem.wasRelative() || isFollowRelativeLinks;
    }

}
