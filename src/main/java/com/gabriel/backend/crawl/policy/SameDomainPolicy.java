package com.gabriel.backend.crawl.policy;

import com.gabriel.backend.crawl.LinkItem;
import java.net.URI;

public class SameDomainPolicy implements CrawlPolicy {

    @Override
    public boolean shouldVisit(LinkItem item) {
        URI root = item.getRootUri();
        URI candidate = item.getAbsoluteNormalizedUri();

        if (root == null || root.getHost() == null || candidate.getHost() == null) {
            return false;
        }

        System.out.println("É de mesmo domínio: " + candidate.getHost() + " e " + root.getHost() + " >> "
                + candidate.getHost().equalsIgnoreCase(root.getHost()));
        return candidate.getHost().equalsIgnoreCase(root.getHost());
    }

}
