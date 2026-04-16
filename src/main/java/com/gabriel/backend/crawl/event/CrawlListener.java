package com.gabriel.backend.crawl.event;

import java.net.URI;

public interface CrawlListener {
    void onStart(String crawlId);
    void onFound(URI uri);
    void onFailed(URI uri);
    void onError(URI uri, Exception e);
    void onAllTasksCompleted();
}

