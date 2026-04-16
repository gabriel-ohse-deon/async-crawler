package com.gabriel.backend.crawl;

import com.gabriel.backend.crawl.normalization.UrlNormalizerChain;
import com.gabriel.backend.crawl.policy.CrawlPolicy;

import java.net.URI;
import java.util.*;
import java.util.concurrent.*;

public class LinkManager {
    private final Set<String> seenUrls;
    private final Queue<LinkItem> toVisit;
    private final UrlNormalizerChain urlNormalizerChain;
    private final RawUrlToURIConverter uriConverter;

    public LinkManager() {
        urlNormalizerChain = new UrlNormalizerChain();
        uriConverter = new RawUrlToURIConverter();

        seenUrls = ConcurrentHashMap.newKeySet();
        toVisit = new ConcurrentLinkedQueue<>();
    }

    public void addInitialLink(URI startUri) {
        createAndAddLink(startUri.toString(), startUri, startUri, startUri);
    }

    public void addLinks(LinkItem parentLinkItem, Set<String> rawLinks, List<CrawlPolicy> policies) {
        if (rawLinks == null) return;
        URI baseUri = parentLinkItem.getAbsoluteNormalizedUri();
        URI rootUri = parentLinkItem.getRootUri();

        for (String rawLink : rawLinks) {
            Optional<URI> convert = uriConverter.convert(baseUri, rawLink);
            convert.ifPresent(uri -> {
                LinkItem item = createLinkItem(rawLink, baseUri, uri, rootUri);
                System.out.println("Antes de aplicar politicas: " + item.getAbsoluteNormalizedUri());
                boolean canVisit = policies.stream().allMatch(p -> p.shouldVisit(item));
                System.out.println("Deve visitar o link acima: " + canVisit);
                if (canVisit) addLink(item);
            });
        }
    }

    private void createAndAddLink(String rawUrl, URI baseUri, URI absoluteUri, URI rootUri) {
        addLink(createLinkItem(rawUrl, baseUri, absoluteUri, rootUri));
    }

    private void addLink(LinkItem item) {
        String key = item.getAbsoluteNormalizedUri().toString();
        if (seenUrls.add(key)) {
            toVisit.add(item);
        }
    }

    private LinkItem createLinkItem(String rawUrl, URI baseUri, URI absoluteUri, URI rootUri) {
        URI normalized = urlNormalizerChain.normalize(absoluteUri);
        return new LinkItem(rawUrl, baseUri, normalized, rootUri);
    }

    public Optional<LinkItem> pollNextLink() {
        LinkItem item;
        while ((item = toVisit.poll()) != null) {
            boolean ok = item.markVisitedIfNot();
            if (ok) {
                return Optional.of(item);
            }
        }
        return Optional.empty();
    }

    public boolean hasNext() {
        return !toVisit.isEmpty();
    }

    public int getPendingCount() {
        return toVisit.size();
    }

    public Set<String> getSeenUrls() {
        return seenUrls;
    }
}