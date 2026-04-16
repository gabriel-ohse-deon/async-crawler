package com.gabriel.backend.repository;

import com.gabriel.backend.crawl.CrawlStatus;

import java.net.URI;
import java.util.Set;

import java.util.concurrent.ConcurrentHashMap;

public class CrawlDocument {
    private final String crawlId;
    private final Set<URI> foundUris = ConcurrentHashMap.newKeySet();
    private final Set<URI> failedUris = ConcurrentHashMap.newKeySet();
    private volatile CrawlStatus status;

    public CrawlDocument(String crawlId) {
        this.crawlId = crawlId;
        this.status = CrawlStatus.ACTIVE;
    }

    public String getCrawlId() {
        return crawlId;
    }

    public Set<URI> getFoundUris() {
        return foundUris;
    }

    public Set<URI> getFailedUris() {
        return failedUris;
    }

    public CrawlStatus getStatus() {
        return status;
    }

    public void setStatus(CrawlStatus status) {
        this.status = status;
    }

    // Adicionais: métodos de conveniência
    public void addFoundUri(URI uri) {
        foundUris.add(uri);
    }

    public void addFailedUri(URI uri) {
        failedUris.add(uri);
    }
}

