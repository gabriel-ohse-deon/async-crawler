package com.gabriel.backend.crawl.rest;

import java.util.List;

public class CrawlResponse {
    private final String id;
    private final String status;
    private final List<String> urls;

    public CrawlResponse(String id, String status, List<String> urls) {
        this.id = id;
        this.status = status;
        this.urls = urls;
    }

    public String getId() {
        return id;
    }

    public String getStatus() {
        return status;
    }

    public List<String> getUrls() {
        return urls;
    }
}
